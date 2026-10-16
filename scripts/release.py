#!/usr/bin/env python3
"""Release bundle verification and publication; uses only Python's standard library and gh."""

import argparse
import hashlib
import json
import os
from pathlib import Path
import plistlib
import re
import subprocess
import tempfile
import zipfile


TARGETS = {
    "windows-x64": "zip",
    "linux-x64": "tar.gz",
    "macos-arm64": "zip",
    "macos-x64": "zip",
}


def require(condition, message):
    if not condition:
        raise RuntimeError(message)


def run(command, **kwargs):
    return subprocess.run(command, check=True, text=True, capture_output=True, **kwargs)


def digest(path):
    result = hashlib.sha256()
    with path.open("rb") as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b""):
            result.update(block)
    return result.hexdigest()


def bundle_names(version):
    return [f"jave7-{version}-{target}.{extension}" for target, extension in TARGETS.items()]


def verify_bundle(archive, metadata, target):
    """Exercise the extracted image from a separate directory, with no Java on PATH."""
    require(target in TARGETS, f"Unsupported target: {target}")
    version = metadata["version"]
    require(archive.name == f"jave7-{version}-{target}.{TARGETS[target]}", "Unexpected archive filename")
    archive = archive.resolve(strict=True)
    platform, architecture = target.split("-")
    with tempfile.TemporaryDirectory(prefix="jave bundle ") as temporary:
        work = Path(temporary)
        extracted = work / "extracted app"
        extracted.mkdir()
        if platform == "macos":
            run(["ditto", "-x", "-k", str(archive), str(extracted)])
        elif platform == "linux":
            run(["tar", "-xzf", str(archive), "-C", str(extracted)])
        else:
            with zipfile.ZipFile(archive) as bundle:
                bundle.extractall(extracted)

        image = extracted / ("JavE.app" if platform == "macos" else "JavE")
        if platform == "macos":
            app = image / "Contents/app"
            runtime = image / "Contents/runtime/Contents/Home"
            launcher = image / "Contents/MacOS/JavE"
            with (image / "Contents/Info.plist").open("rb") as stream:
                info = plistlib.load(stream)
            require(info["CFBundleShortVersionString"] == metadata["nativeVersion"], "Incorrect macOS version")
            require(info["CFBundleVersion"] == metadata["nativeVersion"], "Incorrect macOS build version")
        elif platform == "linux":
            app = image / "lib/app"
            runtime = image / "lib/runtime"
            launcher = image / "bin/JavE"
        else:
            app = image / "app"
            runtime = image / "runtime"
            launcher = image / "JavE.exe"

        for name in ["clipart", "config", "examples", "ext", "fonts"]:
            folder = app / name
            require(folder.is_dir() and any(folder.iterdir()), f"Missing application data: {name}")
        for name in ["README.md", "LICENSE.txt"]:
            require((app / name).is_file(), f"Missing {name}")
        require((runtime / "legal/java.base/LICENSE").is_file(), "Missing bundled runtime license")
        require(launcher.is_file(), "Missing native launcher")
        require(os.access(launcher, os.X_OK), "Native launcher lost executable permissions")
        cfg = (app / "JavE.cfg").read_text(encoding="utf-8")
        require(f"-Djpackage.app-version={metadata['nativeVersion']}" in cfg, "Incorrect native launcher version")

        jar = app / f"jave7-{version}-app.jar"
        with zipfile.ZipFile(jar) as application:
            properties = application.read("de/jave/jave/version/version.properties").decode("utf-8")
            require("@JAVE_" not in properties, "Unexpanded version template")
            values = dict(line.split("=", 1) for line in properties.splitlines() if line and not line.startswith("#"))
            require(values["Version.version"] == version, "Incorrect application version")
            require(values["Version.buildDate"] == metadata["buildDate"], "Incorrect build date")
            manifest = application.read("META-INF/MANIFEST.MF").decode("utf-8").replace("\r\n ", "")
            require(f"Implementation-Version: {version}\r\n" in manifest, "Incorrect manifest version")
            require("de/jave/jave/version/version.properties.template" not in application.namelist(), "Version template leaked into JAR")

        empty = work / "unrelated working directory"
        empty.mkdir()
        environment = os.environ.copy()
        for key in ["JAVA_HOME", "JDK_JAVA_OPTIONS", "JAVA_TOOL_OPTIONS", "_JAVA_OPTIONS"]:
            environment.pop(key, None)
        environment["PATH"] = str(empty)
        java = runtime / ("bin/java.exe" if platform == "windows" else "bin/java")
        expected = f"JavE {version}"
        java_result = run([str(java), "-XshowSettings:properties", "-Djava.awt.headless=true", "-cp", str(jar),
                           "de.jave.jave.Jave", "--version"], cwd=empty, env=environment, timeout=60)
        require(java_result.stdout.strip() == expected, "Packaged runtime reports incorrect version")
        require(re.search(r"java\.version = 21(?:\.|\s)", java_result.stderr), "Expected a Java 21 runtime")
        arch = re.search(r"os\.arch = (\S+)", java_result.stderr)
        require(arch is not None and arch.group(1) in
                ({"aarch64", "arm64"} if architecture == "arm64" else {"amd64", "x86_64"}), "Incorrect runtime architecture")
        native_result = run([str(launcher), "--version"], cwd=empty, env=environment, timeout=60)
        # A Windows GUI launcher may have no stdout handle; the bundled java check
        # above still verifies the full version, and this checks native startup/exit.
        if platform != "windows" or native_result.stdout.strip():
            require(native_result.stdout.strip() == expected, "Native launcher reports incorrect version")
    print(f"Verified {archive.name}: native launcher, Java 21/{architecture}, version, resources, and licenses")


def write_checksums(directory, metadata):
    names = bundle_names(metadata["version"])
    actual = {path.name for path in directory.iterdir()}
    require(actual - {"SHA256SUMS.txt"} == set(names), "Expected exactly the four release bundles")
    sums = directory / "SHA256SUMS.txt"
    sums.write_text("".join(f"{digest(directory / name)}  {name}\n" for name in sorted(names)), encoding="utf-8")
    return [directory / name for name in names] + [sums]


def gh(*arguments):
    return run(["gh", *arguments])


def gh_json(*arguments):
    return json.loads(gh(*arguments).stdout)


def find_release(repository, tag):
    # REST's /releases/tags endpoint only finds published releases. GraphQL
    # also finds drafts by their pending tag, including on a publication retry.
    owner, name = repository.split("/", 1)
    query = """query($owner: String!, $name: String!, $tag: String!) {
      repository(owner: $owner, name: $name) {
        release(tagName: $tag) { databaseId isDraft }
      }
    }"""
    result = gh_json("api", "graphql", "-f", f"query={query}", "-f", f"owner={owner}",
                     "-f", f"name={name}", "-f", f"tag={tag}")
    release = result["data"]["repository"]["release"]
    return None if release is None else {"id": release["databaseId"], "draft": release["isDraft"]}


def publish(directory, metadata, tag, repository):
    require(tag == metadata["tag"] == f"v{metadata['version']}", "Release tag does not match validated metadata")
    require(metadata["buildDate"] != "development", "A release requires the tagged commit's build date")
    files = write_checksums(directory, metadata)
    release = find_release(repository, tag)
    require(release is None or release["draft"], "This release is already published; it will not be modified")
    if release is None:
        arguments = ["release", "create", tag, "--repo", repository, "--verify-tag", "--draft",
                     "--title", f"JavE {metadata['version']}", "--generate-notes"]
        if metadata["prerelease"]:
            arguments.extend(["--prerelease", "--latest=false"])
        gh(*arguments)
        release = find_release(repository, tag)
        require(release is not None and release["draft"], "Could not find the newly created draft")
    gh("release", "upload", tag, "--repo", repository, "--clobber", *(str(path) for path in files))
    uploaded = gh_json("api", f"repos/{repository}/releases/{release['id']}")
    require(uploaded["draft"], "Release was published while assets were being uploaded")
    remote = {asset["name"]: asset for asset in uploaded["assets"]}
    require(set(remote) == {path.name for path in files}, "Draft has missing or unexpected release assets")
    for path in files:
        asset = remote[path.name]
        require(asset["state"] == "uploaded" and asset["size"] == path.stat().st_size, f"Incomplete upload: {path.name}")
        if asset.get("digest"):
            require(asset["digest"] == f"sha256:{digest(path)}", f"Upload checksum mismatch: {path.name}")
        else:
            with tempfile.TemporaryDirectory() as temporary:
                gh("release", "download", tag, "--repo", repository, "--pattern", path.name, "--dir", temporary)
                require(digest(Path(temporary) / path.name) == digest(path), f"Upload checksum mismatch: {path.name}")
    arguments = ["release", "edit", tag, "--repo", repository, "--draft=false",
                 f"--prerelease={str(metadata['prerelease']).lower()}"]
    if metadata["prerelease"]:
        arguments.append("--latest=false")
    gh(*arguments)
    print(f"Published {repository} {tag} with four bundles and SHA256SUMS.txt")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--metadata", type=Path, default=Path("build/release-metadata.json"))
    commands = parser.add_subparsers(dest="command", required=True)
    commands.add_parser("outputs", help="Append validated metadata to GITHUB_OUTPUT")
    verify = commands.add_parser("verify-bundle")
    verify.add_argument("--archive", type=Path, required=True)
    verify.add_argument("--target", choices=TARGETS, required=True)
    checksums = commands.add_parser("checksums")
    checksums.add_argument("--directory", type=Path, required=True)
    publication = commands.add_parser("publish")
    publication.add_argument("--directory", type=Path, required=True)
    publication.add_argument("--tag", required=True)
    publication.add_argument("--repository", default=os.environ.get("GITHUB_REPOSITORY", "mevanlc/jave7"))
    arguments = parser.parse_args()
    metadata = json.loads(arguments.metadata.read_text(encoding="utf-8"))
    if arguments.command == "outputs":
        with open(os.environ["GITHUB_OUTPUT"], "a", encoding="utf-8") as stream:
            for name, value in metadata.items():
                stream.write(f"{name}={str(value).lower() if isinstance(value, bool) else value}\n")
    elif arguments.command == "verify-bundle":
        verify_bundle(arguments.archive, metadata, arguments.target)
    elif arguments.command == "checksums":
        write_checksums(arguments.directory, metadata)
    else:
        publish(arguments.directory, metadata, arguments.tag, arguments.repository)


if __name__ == "__main__":
    try:
        main()
    except (RuntimeError, subprocess.CalledProcessError) as error:
        if isinstance(error, subprocess.CalledProcessError):
            print(error.stdout or "", end="")
            print(error.stderr or "", end="")
        raise SystemExit(str(error)) from error
