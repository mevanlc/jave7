# unofficial http://jave.de fork

(hi spoon!)

## Downloads

Download portable bundles from [GitHub Releases](https://github.com/mevanlc/jave7/releases).
Each includes Java 21, configuration, fonts, clipart, and examples; no separate Java
installation is required.

| Platform | Bundle | Launch after extracting |
| --- | --- | --- |
| Windows x64 | `jave7-<version>-windows-x64.zip` | `JavE/JavE.exe` |
| Linux x64 | `jave7-<version>-linux-x64.tar.gz` | `JavE/bin/JavE` |
| macOS Apple Silicon | `jave7-<version>-macos-arm64.zip` | `JavE.app` |
| macOS Intel | `jave7-<version>-macos-x64.zip` | `JavE.app` |

Keep the extracted bundle together. On macOS, you can move `JavE.app` to
`/Applications`. Linux requires a desktop session and the usual system libraries
for Java's AWT/Swing; the bundle targets glibc-based distributions, not Alpine.
The initial bundles are unsigned and macOS bundles are not notarized, so the OS
may require approval when opening them. “Check for Updates” opens this fork's
releases page, including prereleases.

Each release includes `SHA256SUMS.txt`. To check downloaded archives on macOS or
Linux, place it alongside the bundles and run `shasum -a 256 -c SHA256SUMS.txt`
(or `sha256sum -c SHA256SUMS.txt` on Linux). Entries for bundles you did not
download will report missing files. On Windows, use
`Get-FileHash .\jave7-<version>-windows-x64.zip -Algorithm SHA256` and compare
the result with the corresponding entry.

## Build and release

Builds, tests, and release bundles target **Java 21**, which supplies the grapheme
handling used by the editor. The checked-in Gradle daemon criteria and Java
toolchain select JDK 21, downloading it through the Foojay resolver when needed.
A Java 17 or newer installation is sufficient to start the Gradle wrapper:

```sh
./gradlew check
./gradlew releaseMetadata releaseBundle
```

On Windows, use `gradlew.bat`. `releaseBundle` builds for the host OS and CPU,
placing the archive in `build/distributions/`. Supported targets are Windows x64,
Linux x64, and macOS x64/ARM64. `nativeAppImage` produces the unarchived image
in `build/jpackage/`. The existing `macAppBundle` and
`macDeployToApplications` tasks remain available; the latter installs to
`/Applications`.

Development builds report `7.0.0-dev` with build date `development`. To build a
specific version locally:

```sh
./gradlew check releaseMetadata releaseBundle \
  -PreleaseVersion=7.1.0-rc.1 -PbuildDate=2026-10-16
python3 scripts/release.py verify-bundle \
  --archive build/distributions/jave7-7.1.0-rc.1-macos-arm64.zip \
  --target macos-arm64
```

Use your host's target and archive name in the verification command. It extracts
to a path containing spaces and checks the native launcher, bundled Java,
version metadata, and application data from an unrelated working directory.
`--version` prints the full application version without starting the GUI; for
example, `JavE.app/Contents/MacOS/JavE --version`.

The version template in `src/main/java/de/jave/jave/version/` contains explicit
`@JAVE_VERSION@` and `@JAVE_BUILD_DATE@` tokens. Gradle fills them in generated
resources, together with the JAR manifest and packaging metadata. Release builds
never commit version bumps back to the repository. Native package metadata uses
only `7.x.y`; the application and archive retain prerelease/build identifiers.

Before the first release, push the workflow and run **Actions → Release bundles
→ Run workflow** with a version such as `7.0.0-rc.1` (without `v`). This tests all
four targets and uploads downloadable Actions artifacts without publishing a
GitHub release.

To publish, merge or push the intended release to `main`, then create and push
a tag on that commit:

```sh
git tag -a v7.0.0 -m "JavE 7.0.0"
git push origin v7.0.0
```

Tags must use the `v` prefix and valid SemVer with major **7**. Increment minor
and patch versions; use tags such as `v7.1.0-rc.1` for prereleases. Build metadata
such as `v7.1.0+build.42` is supported. Non-`v` tags do not trigger this workflow;
malformed `v` tags and other major versions fail validation.

The workflow derives the build date from the tagged commit in UTC. All four
targets must pass tests and extracted-bundle verification before publication.
It uploads the bundles and checksums to a draft, verifies the uploaded assets,
then publishes with generated release notes. Prereleases do not replace the
latest stable release. A failed publication can resume an existing draft;
rerunning an already published release fails without changing it. No extra
token is needed: only the publication job receives write access through
`GITHUB_TOKEN`.

## Editor changes

**View > Auto Zoom** fits the whole canvas at the largest zoom step that fits the
available space, adjusting as the window, panels, rulers, font, or canvas changes.
It uses zoom offsets from −10 to +100 and remembers the checkbox across restarts.
Manual zooming turns Auto Zoom off before applying the requested step. If the
canvas cannot fit at the minimum zoom, scrolling remains available. Font sizes
always remain positive.

**View > Reset Zoom** (Cmd+0 on macOS, Ctrl+0 on Windows/Linux) restores the
configured display font size and turns Auto Zoom off. Ctrl+0 also works on the
macOS canvas.

Undo and redo preserve the complete layer stack, including secondary-layer edits,
layer creation, duplication, deletion, order, names, visibility, opacity, and
flattening. Switching the active layer adds no undo step and preserves redo.
Recovery logs retain layers as well as the composite frames used for animations.
