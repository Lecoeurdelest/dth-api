@echo off
REM One-click Docker setup script for Windows
REM Usage: scripts\start.bat

echo Starting DTH API Docker Setup...
echo.

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo Docker is not running. Please start Docker Desktop first.
    exit /b 1
)

REM Change to docker directory
cd docker

echo Cleaning up existing containers...
docker-compose down -v 2>nul

echo.
echo Building Docker images...
docker-compose build --no-cache

echo.
echo Starting containers...
docker-compose up -d

echo.
echo Waiting for MariaDB to be ready...
timeout /t 10 /nobreak >nul

echo.
echo Waiting for application to start...
timeout /t 20 /nobreak >nul

echo.
echo ========================================
echo Setup complete!
echo.
echo Service URLs:
echo   - API: http://localhost:8080
echo   - Swagger UI: http://localhost:8080/swagger-ui.html
echo   - MariaDB: localhost:3306
echo.
echo Admin Login:
echo   - Username: admin
echo   - Password: admin123
echo.
echo Useful commands:
echo   - View logs: docker-compose logs -f
echo   - Stop: docker-compose down
echo   - Rebuild: docker-compose up --build
echo.
echo ========================================
echo.
echo Following logs (Ctrl+C to exit)...
echo.

docker-compose logs -f
