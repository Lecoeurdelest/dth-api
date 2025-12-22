#!/bin/bash

# Development mode with hot-reload
# Uses bind mounts for live code changes
# 
# Usage: ./scripts/start-dev.sh
# Or from project root: bash scripts/start-dev.sh

set -e

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

# Change to project root
cd "$PROJECT_ROOT"

echo "🚀 Starting DTH API in Development Mode (Hot Reload)..."
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop first."
    exit 1
fi

# Stop existing containers
echo "🧹 Cleaning up existing containers..."
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml down 2>/dev/null || true

# Build development image
echo ""
echo "🔨 Building development Docker image..."
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml build app

# Start MariaDB first
echo ""
echo "🚀 Starting MariaDB..."
docker-compose -f docker/docker-compose.yml up -d mariadb

# Wait for MariaDB
echo ""
echo "⏳ Waiting for MariaDB..."
timeout=60
counter=0
until docker-compose -f docker/docker-compose.yml exec -T mariadb mysqladmin ping -h localhost --silent 2>/dev/null; do
    sleep 2
    counter=$((counter + 2))
    if [ $counter -ge $timeout ]; then
        echo "❌ MariaDB failed to start"
        exit 1
    fi
    echo -n "."
done
echo ""
echo -e "${GREEN}✅ MariaDB is ready!${NC}"

# Start application in development mode
echo ""
echo "🚀 Starting application in development mode..."
echo -e "${YELLOW}💡 Code changes will be automatically reloaded!${NC}"
echo ""

docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml up app

