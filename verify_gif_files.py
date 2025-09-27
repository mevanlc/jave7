#!/usr/bin/env python3
import os
import sys
from pathlib import Path
import filecmp
import shutil

def verify_gif_files():
    bin_dir = Path("bin/main")
    src_dir = Path("src/main/java")

    if not bin_dir.exists():
        print(f"Error: {bin_dir} directory not found")
        return 1

    if not src_dir.exists():
        print(f"Error: {src_dir} directory not found")
        return 1

    missing_files = []
    mismatched_files = []
    matched_files = []
    copied_files = []
    src_only_files = []

    # First, walk through all GIF files in bin/main directory
    bin_gifs = {}
    for bin_file in bin_dir.rglob("*.gif"):
        if bin_file.is_file():
            rel_path = bin_file.relative_to(bin_dir)
            bin_gifs[rel_path] = bin_file

    # Check each bin GIF against src
    for rel_path, bin_file in bin_gifs.items():
        src_file = src_dir / rel_path

        if not src_file.exists():
            missing_files.append(rel_path)
        else:
            # Compare file contents
            bin_size = bin_file.stat().st_size
            src_size = src_file.stat().st_size

            if filecmp.cmp(bin_file, src_file, shallow=False):
                matched_files.append(rel_path)
            elif src_size == 0 and bin_size > 0:
                # Copy the bin file over the 0-byte src file
                shutil.copy2(bin_file, src_file)
                copied_files.append((rel_path, bin_size))
            elif src_size != bin_size:
                mismatched_files.append((rel_path, f"size mismatch: src={src_size}, bin={bin_size}"))
            else:
                mismatched_files.append((rel_path, "content differs"))

    # Also check for GIF files that exist in src but not in bin
    for src_file in src_dir.rglob("*.gif"):
        if src_file.is_file():
            rel_path = src_file.relative_to(src_dir)
            if rel_path not in bin_gifs:
                src_size = src_file.stat().st_size
                src_only_files.append((rel_path, src_size))

    # Print results
    print(f"=== GIF Files Verification Results ===")
    print(f"Total GIF files in bin/main: {len(bin_gifs)}")
    print(f"✓ Matched: {len(matched_files)}")
    print(f"✓ Copied (0-byte replacements): {len(copied_files)}")
    print(f"✗ Missing in src: {len(missing_files)}")
    print(f"✗ Mismatched: {len(mismatched_files)}")
    print(f"⚠ In src only (not in bin): {len(src_only_files)}")

    if copied_files:
        print(f"\n=== Copied Files (replaced 0-byte files) ===")
        for f, size in sorted(copied_files):
            print(f"  ✓ {f} ({size} bytes)")

    if missing_files:
        print(f"\n=== Missing Files (in bin but not in src) ===")
        for f in sorted(missing_files):
            bin_file = bin_gifs[f]
            size = bin_file.stat().st_size
            print(f"  - {f} ({size} bytes in bin)")

    if mismatched_files:
        print(f"\n=== Mismatched Files (different content) ===")
        for f, reason in sorted(mismatched_files):
            print(f"  - {f}: {reason}")

    if src_only_files:
        print(f"\n=== Files only in src (not in bin) ===")
        for f, size in sorted(src_only_files):
            if size == 0:
                print(f"  - {f} (0 bytes - likely missing)")
            else:
                print(f"  - {f} ({size} bytes)")

    if matched_files and len(matched_files) <= 20:
        print(f"\n=== Matched Files ===")
        for f in sorted(matched_files):
            print(f"  ✓ {f}")
    elif matched_files:
        print(f"\n=== Matched Files ===")
        print(f"  {len(matched_files)} files match perfectly (too many to list)")

    # Return exit code
    return 0 if (not missing_files and not mismatched_files and all(size > 0 for _, size in src_only_files)) else 1

if __name__ == "__main__":
    sys.exit(verify_gif_files())