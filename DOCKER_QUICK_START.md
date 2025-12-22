# Docker Quick Start

## One-Click Start

```bash
./start.sh
```

That's it! 🎉

## What It Does

1. ✅ Builds Docker images
2. ✅ Starts MariaDB database
3. ✅ Starts Spring Boot application
4. ✅ Shows logs

## Access Your Services

- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **Database**: localhost:3306 (user: root, password: root)

## Common Commands

```bash
# Start
./start.sh

# Development mode (hot reload)
./start-dev.sh

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Restart
docker-compose restart
```

## Using Make (Alternative)

```bash
make start      # Start everything
make dev        # Development mode
make logs       # View logs
make down       # Stop
make clean      # Clean everything
```

## Docker Sync (Optional - Mac/Windows)

For better performance on Mac/Windows:

```bash
# Install
gem install docker-sync

# Start (auto-detected by start.sh)
./start.sh
```

## Troubleshooting

**Port in use?**
- Change ports in `docker-compose.yml`

**Database issues?**
```bash
docker-compose logs mariadb
docker-compose restart mariadb
```

**App won't start?**
```bash
docker-compose logs app
docker-compose build --no-cache app
```

For more details, see [DOCKER_SETUP.md](./DOCKER_SETUP.md)

