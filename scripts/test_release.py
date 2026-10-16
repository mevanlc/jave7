"""Publication must never expose partial bundles or alter a published release."""

from pathlib import Path
import subprocess
import tempfile
import unittest
from unittest.mock import patch

import release


class PublicationTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.directory = Path(self.temporary.name)
        self.metadata = {"version": "7.1.2-rc.1+build.42", "nativeVersion": "7.1.2", "prerelease": True,
                         "buildDate": "2026-10-16", "tag": "v7.1.2-rc.1+build.42"}
        for name in release.bundle_names(self.metadata["version"]):
            (self.directory / name).write_bytes(name.encode("utf-8"))

    def publish(self):
        release.publish(self.directory, self.metadata, self.metadata["tag"], "example/jave7")

    def uploaded(self):
        return {"draft": True, "assets": [
            {"name": path.name, "state": "uploaded", "size": path.stat().st_size,
             "digest": "sha256:" + release.digest(path)} for path in self.directory.iterdir()
        ]}

    def found(self, draft=True):
        return {"data": {"repository": {"release": {"databaseId": 123, "isDraft": draft}}}}

    def missing_then_created(self, *arguments):
        if arguments[:2] == ("api", "graphql"):
            if self.lookup_count == 0:
                self.lookup_count += 1
                return {"data": {"repository": {"release": None}}}
            return self.found()
        return self.uploaded()

    @patch("release.gh")
    @patch("release.gh_json")
    def test_missing_target_prevents_any_github_access(self, api, gh):
        next(self.directory.iterdir()).unlink()
        with self.assertRaisesRegex(RuntimeError, "four release bundles"):
            self.publish()
        api.assert_not_called()
        gh.assert_not_called()

    @patch("release.gh")
    @patch("release.gh_json")
    def test_published_release_cannot_be_modified(self, api, gh):
        api.return_value = self.found(draft=False)
        with self.assertRaisesRegex(RuntimeError, "already published"):
            self.publish()
        gh.assert_not_called()

    @patch("release.gh")
    @patch("release.gh_json")
    def test_retry_resumes_draft_and_publishes_only_verified_assets(self, api, gh):
        api.side_effect = lambda *args: self.found() if args[:2] == ("api", "graphql") else self.uploaded()
        self.publish()
        commands = [call.args for call in gh.call_args_list]
        self.assertEqual([command[1] for command in commands], ["upload", "edit"])
        self.assertIn("--clobber", commands[0])
        self.assertIn("--draft=false", commands[-1])
        self.assertIn("--prerelease=true", commands[-1])
        self.assertIn("--latest=false", commands[-1])
        self.assertEqual(("api", "graphql"), api.call_args_list[0].args[:2])
        self.assertEqual(("api", "repos/example/jave7/releases/123"), api.call_args.args)
        sums = (self.directory / "SHA256SUMS.txt").read_text().splitlines()
        self.assertEqual(4, len(sums))
        for line in sums:
            checksum, name = line.split("  ", 1)
            self.assertEqual(release.digest(self.directory / name), checksum)

    @patch("release.gh")
    @patch("release.gh_json")
    def test_new_prerelease_is_created_as_draft(self, api, gh):
        self.lookup_count = 0
        api.side_effect = self.missing_then_created
        self.publish()
        create = gh.call_args_list[0].args
        self.assertEqual(("release", "create"), create[:2])
        for option in ["--draft", "--verify-tag", "--generate-notes", "--prerelease", "--latest=false"]:
            self.assertIn(option, create)

    @patch("release.gh")
    @patch("release.gh_json")
    def test_bad_remote_digest_keeps_release_as_draft(self, api, gh):
        def response(*args, **kwargs):
            result = self.uploaded()
            result["assets"][0]["digest"] = "sha256:incorrect"
            return self.found() if args[:2] == ("api", "graphql") else result
        api.side_effect = response
        with self.assertRaisesRegex(RuntimeError, "checksum mismatch"):
            self.publish()
        self.assertEqual(["upload"], [call.args[1] for call in gh.call_args_list])

    @patch("release.gh")
    @patch("release.gh_json")
    def test_upload_failure_cannot_publish(self, api, gh):
        api.return_value = self.found()
        gh.side_effect = subprocess.CalledProcessError(1, ["gh", "release", "upload"], stderr="network failure")
        with self.assertRaises(subprocess.CalledProcessError):
            self.publish()
        self.assertEqual(["upload"], [call.args[1] for call in gh.call_args_list])

    @patch("release.gh")
    @patch("release.gh_json")
    def test_stable_release_uses_normal_latest_selection(self, api, gh):
        for old, new in zip(release.bundle_names(self.metadata["version"]), release.bundle_names("7.1.2")):
            (self.directory / old).rename(self.directory / new)
        self.metadata["version"] = "7.1.2"
        self.metadata["tag"] = "v7.1.2"
        self.metadata["prerelease"] = False
        self.lookup_count = 0
        api.side_effect = self.missing_then_created
        self.publish()
        for call in gh.call_args_list:
            self.assertNotIn("--latest=false", call.args)
        self.assertIn("--prerelease=false", gh.call_args.args)

    @patch("release.gh")
    @patch("release.gh_json")
    def test_mismatched_tag_prevents_publication(self, api, gh):
        self.metadata["tag"] = "v7.1.3"
        with self.assertRaisesRegex(RuntimeError, "tag does not match"):
            self.publish()
        api.assert_not_called()
        gh.assert_not_called()


if __name__ == "__main__":
    unittest.main()
