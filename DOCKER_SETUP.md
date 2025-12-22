# Docker Setup Guide

This guide explains how to run the DTH API project using Docker with MariaDB and Docker Sync for optimal development experience.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose (included with Docker Desktop)
- (Optional) Docker Sync for better performance on Mac/Windows

## Quick Start (One-Click)

### Option 1: Using the Start Script (Recommended)

```bash
./start.sh
```

This script will:
- ✅ Check Docker is running
- ✅ Clean up existing containers
- ✅ Build Docker images
- ✅ Start MariaDB and wait for it to be ready
- ✅ Start the Spring Boot application
- ✅ Show logs

### Option 2: Using Make

```bash
make start
```

### Option 3: Using Docker Compose Directly

```bash
docker-compose up --build
```

## Development Mode (Hot Reload)

For development with automatic code reloading:

```bash
./start-dev.sh
```

Or using Make:

```bash
make dev
```

This mode:
- Uses bind mounts for live code changes
- Automatically rebuilds on file changes
- Faster iteration cycle

## Docker Sync Setup (Optional - Recommended for Mac/Windows)

Docker Sync improves file system performance on Mac and Windows.

### Install Docker Sync

```bash
gem install docker-sync
```

### Start with Docker Sync

```bash
# Start docker-sync
docker-sync start

# In another terminal, start docker-compose
docker-compose -f docker-compose.yml -f docker-compose.sync.yml up
```

Or use the start script which auto-detects docker-sync:

```bash
./start.sh
```

## Available Commands

### Using Makefile

```bash
make help          # Show all available commands
make build         # Build Docker images
make up            # Start containers
make down          # Stop containers
make logs          # Show logs
make restart       # Restart containers
make clean         # Stop and remove volumes
make dev           # Development mode
make db-logs       # MariaDB logs only
make app-logs      # Application logs only
make shell         # Open shell in app container
make db-shell      # Open MariaDB shell
make rebuild        # Rebuild and restart
```

### Using Docker Compose Directly

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild
docker-compose up --build

# View specific service logs
docker-compose logs -f app
docker-compose logs -f mariadb
```

## Service URLs

Once started, access services at:

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **MariaDB**: localhost:3306

## Database Connection

**From Host Machine:**
- Host: `localhost`
- Port: `3306`
- Database: `app_db`
- Username: `root`
- Password: `root`

**From Docker Container:**
- Host: `mariadb`
- Port: `3306`
- Database: `app_db`
- Username: `root`
- Password: `root`

## File Structure

```
dth-api/
├── Dockerfile              # Production Docker image
├── Dockerfile.dev          # Development Docker image
├── docker-compose.yml      # Main compose file
├── docker-compose.dev.yml  # Development overrides
├── docker-sync.yml         # Docker sync configuration
├── .dockerignore           # Files to exclude from Docker
├── start.sh                # One-click start script
├── start-dev.sh            # Development start script
└── Makefile                # Make commands
```

## Troubleshooting

### Port Already in Use

If port 8080 or 3306 is already in use:

1. Stop the conflicting service
2. Or change ports in `docker-compose.yml`:
   ```yaml
   ports:
     - "8081:8080"  # Change host port
   ```

### Database Connection Issues

1. Check MariaDB is healthy:
   ```bash
   docker-compose ps
   ```

2. Check MariaDB logs:
   ```bash
   docker-compose logs mariadb
   ```

3. Restart MariaDB:
   ```bash
   docker-compose restart mariadb
   ```

### Application Won't Start

1. Check application logs:
   ```bash
   docker-compose logs app
   ```

2. Rebuild the image:
   ```bash
   docker-compose build --no-cache app
   docker-compose up -d
   ```

### Docker Sync Issues

1. Stop docker-sync:
   ```bash
   docker-sync stop
   ```

2. Clean sync volumes:
   ```bash
   docker-sync clean
   ```

3. Restart:
   ```bash
   docker-sync start
   ```

### Clean Everything

To completely reset:

```bash
make clean
# Or manually:
docker-compose down -v
docker system prune -f
```

## Development Workflow

1. **Start services:**
   ```bash
   ./start-dev.sh
   ```

2. **Make code changes** - Changes are automatically synced

3. **View logs:**
   ```bash
   docker-compose logs -f app
   ```

4. **Access database:**
   ```bash
   make db-shell
   ```

5. **Stop services:**
   ```bash
   docker-compose down
   ```

## Production Build

For production, use the production Dockerfile:

```bash
docker build -t dth-api:latest .
docker run -p 8080:8080 --env-file .env dth-api:latest
```

## Notes

- Database data persists in Docker volume `mariadb_data`
- To reset database: `docker-compose down -v`
- Application uses profile `docker` when running in Docker
- Flyway migrations run automatically on startup

