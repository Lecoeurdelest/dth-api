#!/bin/bash

# Development mode with hot-reload
# Uses bind mounts or docker-sync for live code changes
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
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Enable Docker BuildKit for faster builds
export DOCKER_BUILDKIT=1
export COMPOSE_DOCKER_CLI_BUILD=1

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop first."
    exit 1
fi

# Docker-sync removed - using standard bind mounts only
# Stop existing containers
echo ""
echo "🧹 Cleaning up existing containers..."
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml down 2>/dev/null || true

    COMPOSE_FILES="-f docker/docker-compose.yml -f docker/docker-compose.dev.yml"

# Build development image (force rebuild to ensure using Dockerfile.dev)
echo ""
echo "🔨 Building development Docker image (using BuildKit cache)..."
# Remove old container if exists to force rebuild
docker-compose $COMPOSE_FILES rm -f app 2>/dev/null || true
docker-compose $COMPOSE_FILES build --progress=plain app || docker-compose $COMPOSE_FILES build app

# Using standard bind mounts - no docker-sync needed

# Start MariaDB first
echo ""
echo "🚀 Starting MariaDB..."
docker-compose $COMPOSE_FILES up -d mariadb

# Wait for MariaDB with better error handling
echo ""
echo "⏳ Waiting for MariaDB to be ready..."
timeout=60
counter=0
until docker-compose $COMPOSE_FILES exec -T mariadb mysqladmin ping -h localhost --silent 2>/dev/null || \
      docker-compose $COMPOSE_FILES ps mariadb | grep -q "healthy"; do
    sleep 2
    counter=$((counter + 2))
    if [ $counter -ge $timeout ]; then
        echo ""
        echo "❌ MariaDB failed to start within $timeout seconds"
        docker-compose $COMPOSE_FILES logs mariadb
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

docker-compose $COMPOSE_FILES up app

