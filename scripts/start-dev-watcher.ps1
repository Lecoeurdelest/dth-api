# PowerShell script for Windows - Development mode with Hot Reload + Auto-compile
# Optimized: Auto-compile watcher + Spring Boot with DevTools
# Usage: .\scripts\start-dev-watcher.ps1
# Or: powershell -ExecutionPolicy Bypass -File .\scripts\start-dev-watcher.ps1

$ErrorActionPreference = "Stop"

# Colors - Using ASCII-safe characters for Windows PowerShell compatibility
function Write-Success { Write-Host "[OK] $args" -ForegroundColor Green }
function Write-Info { Write-Host "[*] $args" -ForegroundColor Cyan }
function Write-Warning { Write-Host "[!] $args" -ForegroundColor Yellow }
function Write-Error-Message { Write-Host "[ERROR] $args" -ForegroundColor Red }

# Get script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir

# Change to project root
Set-Location $ProjectRoot

Write-Host ""
Write-Host "[*] Starting DTH API in Development Mode (Hot Reload + Auto-compile)..." -ForegroundColor Cyan
Write-Host ""

# Enable Docker BuildKit for faster builds
$env:DOCKER_BUILDKIT = "1"
$env:COMPOSE_DOCKER_CLI_BUILD = "1"

# Check if Docker is running
Write-Info "Checking Docker..."
try {
    docker info | Out-Null
    Write-Success "Docker is running"
} catch {
    Write-Error-Message "Docker is not running. Please start Docker Desktop first."
    exit 1
}

# Compose files
$ComposeFiles = "-f docker/docker-compose.yml", "-f docker/docker-compose.dev.yml"
$ComposeCmd = "docker-compose $($ComposeFiles -join ' ')"

# Stop existing containers
Write-Host ""
Write-Info "Cleaning up existing containers..."
Invoke-Expression "$ComposeCmd down" 2>&1 | Out-Null
Write-Success "Containers cleaned up"

# Build only if needed (incremental build with cache)
Write-Host ""
Write-Info "Checking/Building development image..."
Write-Host "[*] Optimized: Docker will automatically use cache for unchanged layers" -ForegroundColor Gray
Write-Host "[*] Only rebuilds what has changed (much faster!)" -ForegroundColor Gray
Write-Host ""

# Build with cache
Write-Info "Running: $ComposeCmd build app"
Invoke-Expression "$ComposeCmd build app"

if ($LASTEXITCODE -ne 0) {
    Write-Error-Message "Build failed!"
    Write-Info "Try: $ComposeCmd build --no-cache app (clean build)"
    exit 1
}

Write-Success "Build completed (Docker used cache for unchanged layers)"

# Start MariaDB first
Write-Host ""
Write-Info "Starting MariaDB..."
Invoke-Expression "$ComposeCmd up -d mariadb" | Out-Null

# Wait for MariaDB to be ready
Write-Host ""
Write-Info "Waiting for MariaDB to be ready..."
$MaxWait = 60
$Waited = 0
$Ready = $false

while ($Waited -lt $MaxWait -and -not $Ready) {
    Start-Sleep -Seconds 2
    $Waited += 2
    
    try {
        $HealthCheck = docker exec dth-mariadb mysqladmin ping -h localhost --silent 2>&1
        if ($LASTEXITCODE -eq 0) {
            $Ready = $true
        } else {
            Write-Host "." -NoNewline
        }
    } catch {
        Write-Host "." -NoNewline
    }
}

if ($Ready) {
    Write-Host ""
    Write-Success "MariaDB is ready!"
} else {
    Write-Host ""
    Write-Error-Message "MariaDB failed to start within $MaxWait seconds"
    Write-Info "Check logs with: $ComposeCmd logs mariadb"
    exit 1
}

# Start auto-compile watcher in background
Write-Host ""
Write-Info "Starting auto-compile watcher (background)..."
$LogFile = "$env:TEMP\dth-compile.log"

$WatcherJob = Start-Job -ScriptBlock {
    Set-Location $using:ProjectRoot
    if (Test-Path ".\gradlew.bat") {
        .\gradlew.bat build --continuous -x test *> $using:LogFile 2>&1
    } elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
        gradle build --continuous -x test *> $using:LogFile 2>&1
    }
}

Start-Sleep -Seconds 2

# Start application in development mode with hot reload
Write-Host ""
Write-Success "Starting application in development mode..."
Write-Host "[*] Code changes will be automatically compiled and reloaded!" -ForegroundColor Yellow
Write-Host "[*] Edit files in src/ directory and they will reload automatically" -ForegroundColor Yellow
Write-Host ""
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Cyan
Write-Host "   - API: http://localhost:8080"
Write-Host "   - Swagger UI: http://localhost:8080/swagger-ui.html"
Write-Host "   - MariaDB: localhost:3306"
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor Cyan
Write-Host "   - View logs: $ComposeCmd logs -f app"
Write-Host "   - View compile logs: Get-Content $LogFile -Wait"
Write-Host "   - Stop: $ComposeCmd down"
Write-Host "   - Stop watcher: Stop-Job $($WatcherJob.Id); Remove-Job $($WatcherJob.Id)"
Write-Host ""
Write-Host "Press Ctrl+C to stop..."
Write-Host ""

# Cleanup function
$null = Register-EngineEvent PowerShell.Exiting -Action {
    Stop-Job $WatcherJob.Id -ErrorAction SilentlyContinue
    Remove-Job $WatcherJob.Id -ErrorAction SilentlyContinue
}

try {
    # Start app in foreground to see logs
    Invoke-Expression "$ComposeCmd up app"
} finally {
    # Cleanup on exit
    Stop-Job $WatcherJob.Id -ErrorAction SilentlyContinue
    Remove-Job $WatcherJob.Id -ErrorAction SilentlyContinue
}

