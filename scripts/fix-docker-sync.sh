#!/bin/bash

# Script to fix docker-sync volume sync issues
# Usage: ./scripts/fix-docker-sync.sh

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

cd "$PROJECT_ROOT"

echo "🔧 Fixing docker-sync volume sync issues..."
echo ""

# Stop containers
echo "1. Stopping containers..."
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml down 2>/dev/null || true

# Stop docker-sync
echo "2. Stopping docker-sync..."
docker-sync stop -c docker-sync.yml 2>/dev/null || true

# Clean docker-sync
echo "3. Cleaning docker-sync..."
docker-sync clean -c docker-sync.yml 2>/dev/null || true

# Remove volume if exists
echo "4. Removing old volume..."
docker volume rm dth-api-sync 2>/dev/null || true

# Start docker-sync fresh
echo "5. Starting docker-sync fresh..."
docker-sync start -c docker-sync.yml

# Wait for sync
echo "6. Waiting for initial sync (this may take 30-60 seconds)..."
sleep 15

# Check sync status
echo "7. Checking sync status..."
docker-sync list

echo ""
echo "✅ Docker-sync fix complete!"
echo ""
echo "You can now start containers with:"
echo "  ./scripts/start-dev.sh"
echo ""
echo "Or manually:"
echo "  docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml -f docker/docker-compose.sync.yml up"
