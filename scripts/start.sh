#!/bin/bash

# One-click Docker setup script
# This script builds and starts all Docker containers
# 
# Usage: ./scripts/start.sh
# Or from project root: bash scripts/start.sh

set -e

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

# Change to project root
cd "$PROJECT_ROOT"

echo "🚀 Starting DTH API Docker Setup..."
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
 
# Use standard Docker Compose files (include override for development)
COMPOSE_FILES="-f docker/docker-compose.yml"
# Check if override file exists, use it if available
if [ -f "docker/docker-compose.override.yml" ]; then
    COMPOSE_FILES="$COMPOSE_FILES -f docker/docker-compose.override.yml"
fi

# Stop and remove existing containers
echo ""
echo "🧹 Cleaning up existing containers..."
docker-compose $COMPOSE_FILES down -v 2>/dev/null || true

# Build and start containers
echo ""
echo "🔨 Building Docker images..."
docker-compose $COMPOSE_FILES build --no-cache

echo ""
echo "🚀 Starting containers..."
docker-compose $COMPOSE_FILES up -d

# Wait for MariaDB to be ready
echo ""
echo "⏳ Waiting for MariaDB to be ready..."
timeout=60
counter=0
until docker-compose $COMPOSE_FILES exec -T mariadb mysqladmin ping -h localhost --silent 2>/dev/null || docker-compose $COMPOSE_FILES ps mariadb | grep -q "healthy"; do
    sleep 2
    counter=$((counter + 2))
    if [ $counter -ge $timeout ]; then
        echo "❌ MariaDB failed to start within $timeout seconds"
        docker-compose $COMPOSE_FILES logs mariadb
        exit 1
    fi
    echo -n "."
done
echo ""
echo -e "${GREEN}✅ MariaDB is ready!${NC}"

# Wait for application to be ready
echo ""
echo "⏳ Waiting for application to start..."
timeout=120
counter=0
until curl -f http://localhost:8080/actuator/health 2>/dev/null || docker-compose $COMPOSE_FILES exec -T app curl -f http://localhost:8080/actuator/health 2>/dev/null; do
    sleep 3
    counter=$((counter + 3))
    if [ $counter -ge $timeout ]; then
        echo "⚠️  Application might still be starting. Check logs with: docker-compose $COMPOSE_FILES logs app"
        break
    fi
    echo -n "."
done

echo ""
echo ""
echo -e "${GREEN}✅ Setup complete!${NC}"
echo ""
echo "📋 Service URLs:"
echo "   - API: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   - MariaDB: localhost:3306"
echo ""
echo "📝 Useful commands:"
echo "   - View logs: docker-compose logs -f"
echo "   - Stop: docker-compose down"
echo "   - Rebuild: docker-compose up --build"
echo ""
echo "🔍 Following logs (Ctrl+C to exit)..."
echo ""

# Follow logs
docker-compose $COMPOSE_FILES logs -f

