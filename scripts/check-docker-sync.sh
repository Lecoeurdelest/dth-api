#!/bin/bash

# Helper script to check if docker-sync has synced files properly
# Usage: ./scripts/check-docker-sync.sh

set -e

VOLUME_NAME="dth-api-sync"
REQUIRED_FILES=("build.gradle.kts" "settings.gradle.kts" "gradle")

echo "🔍 Checking docker-sync volume: $VOLUME_NAME"

# Check if volume exists
if ! docker volume inspect "$VOLUME_NAME" > /dev/null 2>&1; then
    echo "❌ Volume $VOLUME_NAME does not exist"
    exit 1
fi

echo "✅ Volume exists"

# Try to check files in volume using a temporary container
echo "🔍 Checking files in volume..."

TEMP_CONTAINER=$(docker run -d --rm -v "$VOLUME_NAME":/check busybox sleep 60)

# Wait a bit for container to start
sleep 1

ALL_FILES_EXIST=true
for file in "${REQUIRED_FILES[@]}"; do
    if docker exec "$TEMP_CONTAINER" test -e "/check/$file" 2>/dev/null; then
        echo "✅ Found: $file"
    else
        echo "❌ Missing: $file"
        ALL_FILES_EXIST=false
    fi
done

# Cleanup
docker stop "$TEMP_CONTAINER" > /dev/null 2>&1 || true

if [ "$ALL_FILES_EXIST" = true ]; then
    echo "✅ All required files are present in docker-sync volume"
    exit 0
else
    echo "❌ Some required files are missing. Please ensure docker-sync has synced files."
    echo "   Try: docker-sync start -c docker-sync.yml"
    exit 1
fi

