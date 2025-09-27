#!/usr/bin/env python3
import os
import sys
from pathlib import Path
import filecmp
import shutil

def verify_ext_files():
    ext_dir = Path("ext")
    src_dir = Path("src/main/java")

    if not ext_dir.exists():
        print(f"Error: {ext_dir} directory not found")
        return 1

    if not src_dir.exists():
        print(f"Error: {src_dir} directory not found")
        return 1

    missing_files = []
    mismatched_files = []
    matched_files = []
    copied_files = []

    # Walk through all files in ext/ directory
    for ext_file in ext_dir.rglob("*"):
        if ext_file.is_file():
            # Get relative path from ext/
            rel_path = ext_file.relative_to(ext_dir)

            # Construct corresponding path in src/main/java/
            src_file = src_dir / rel_path

            if not src_file.exists():
                missing_files.append(rel_path)
            else:
                # Compare file contents
                if filecmp.cmp(ext_file, src_file, shallow=False):
                    matched_files.append(rel_path)
                else:
                    # Check if files are both empty (0 bytes)
                    ext_size = ext_file.stat().st_size
                    src_size = src_file.stat().st_size

                    if src_size == 0 and ext_size > 0:
                        # Copy the ext file over the 0-byte src file
                        shutil.copy2(ext_file, src_file)
                        copied_files.append((rel_path, ext_size))
                    elif src_size != ext_size:
                        mismatched_files.append((rel_path, f"size mismatch: src={src_size}, ext={ext_size}"))
                    else:
                        mismatched_files.append((rel_path, "content differs"))

    # Print results
    print(f"=== Verification Results ===")
    print(f"Total files in ext/: {len(matched_files) + len(missing_files) + len(mismatched_files) + len(copied_files)}")
    print(f"✓ Matched: {len(matched_files)}")
    print(f"✓ Copied (0-byte replacements): {len(copied_files)}")
    print(f"✗ Missing: {len(missing_files)}")
    print(f"✗ Mismatched: {len(mismatched_files)}")

    if copied_files:
        print(f"\n=== Copied Files (replaced 0-byte files) ===")
        for f, size in sorted(copied_files):
            print(f"  ✓ {f} ({size} bytes)")

    if missing_files:
        print(f"\n=== Missing Files (not in src/main/java/) ===")
        for f in sorted(missing_files):
            print(f"  - {f}")

    if mismatched_files:
        print(f"\n=== Mismatched Files (different content) ===")
        for f, reason in sorted(mismatched_files):
            print(f"  - {f}: {reason}")

    if matched_files:
        print(f"\n=== Matched Files ===")
        for f in sorted(matched_files):
            print(f"  ✓ {f}")

    # Return exit code
    return 0 if (not missing_files and not mismatched_files) else 1

if __name__ == "__main__":
    sys.exit(verify_ext_files())