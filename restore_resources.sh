#!/bin/bash

# Script to restore 0-byte resource files from bin/main counterparts

count_restored=0
count_notfound=0

echo "Finding and restoring 0-byte resource files..."
echo "==========================================="

# Find all 0-byte files in src/main/java (excluding .java files)
while IFS= read -r zero_file; do
    # Get the relative path from src/main/java
    relative_path="${zero_file#src/main/java/}"

    # Check if a non-zero version exists in bin/main
    bin_file="bin/main/${relative_path}"

    if [ -f "$bin_file" ]; then
        # Get the size of the bin file
        bin_size=$(stat -f%z "$bin_file" 2>/dev/null || stat -c%s "$bin_file" 2>/dev/null)

        if [ "$bin_size" -gt 0 ]; then
            echo "Restoring: $relative_path (${bin_size} bytes)"
            cp "$bin_file" "$zero_file"
            ((count_restored++))
        else
            echo "  SKIP: $relative_path (bin file also 0 bytes)"
        fi
    else
        echo "  NOT FOUND: $relative_path"
        ((count_notfound++))
    fi
done < <(find src/main/java -type f ! -name "*.java" -size 0)

echo "==========================================="
echo "Summary:"
echo "  Restored: $count_restored files"
echo "  Not found: $count_notfound files"

# Rebuild if any files were restored
if [ $count_restored -gt 0 ]; then
    echo ""
    echo "Rebuilding project to update build/resources..."
    ./gradlew clean build
fi