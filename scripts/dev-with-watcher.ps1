# Start dev mode with auto-compile watcher in background (Windows PowerShell)
# This provides seamless hot reload experience

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir
Set-Location $ProjectRoot

Write-Host ""
Write-Host "[*] Starting development mode with auto-compile watcher..." -ForegroundColor Cyan
Write-Host ""
# Note: MariaDB is already started by 'make dev' or start-dev.ps1

# Start auto-compile watcher in background
Write-Host "[*] Starting auto-compile watcher (background)..." -ForegroundColor Cyan

# PowerShell job to run watcher in background
$WatcherJob = Start-Job -ScriptBlock {
    Set-Location $using:ProjectRoot
    $LogFile = "$env:TEMP\dth-compile.log"
    if (Test-Path ".\gradlew.bat") {
        .\gradlew.bat build --continuous -x test *> $LogFile
    } elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
        gradle build --continuous -x test *> $LogFile
    }
}

# Wait a moment for initial compile
Start-Sleep -Seconds 2

# Find Java 17 (try common locations on Windows)
$Java17Home = $null
$PossiblePaths = @(
    "$env:ProgramFiles\Java\jdk-17",
    "${env:ProgramFiles(x86)}\Java\jdk-17",
    "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-17*",
    "C:\Program Files\Eclipse Adoptium\jdk-17*"
)

foreach ($Path in $PossiblePaths) {
    if (Test-Path $Path) {
        $JavaExe = Get-ChildItem -Path $Path -Filter "java.exe" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($JavaExe) {
            $Java17Home = $JavaExe.Directory.Parent.FullName
            break
        }
    }
}

# Start Spring Boot
Write-Host "[OK] Starting Spring Boot (profile: dev)..." -ForegroundColor Green
Write-Host "    Hot reload: Save file -> Auto compile -> DevTools restart (~2-5s)" -ForegroundColor Yellow
Write-Host "    Compile logs: Get-Content $env:TEMP\dth-compile.log -Wait" -ForegroundColor Gray
Write-Host "    Stop watcher: Stop-Job $($WatcherJob.Id); Remove-Job $($WatcherJob.Id)" -ForegroundColor Gray
Write-Host ""

# Cleanup function
$null = Register-EngineEvent PowerShell.Exiting -Action {
    Stop-Job $WatcherJob.Id -ErrorAction SilentlyContinue
    Remove-Job $WatcherJob.Id -ErrorAction SilentlyContinue
}

try {
    # Start bootRun
    if ($Java17Home) {
        $env:JAVA_HOME = $Java17Home
        $env:SPRING_PROFILES_ACTIVE = "dev"
        
        if (Test-Path ".\gradlew.bat") {
            .\gradlew.bat bootRun
        } elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
            gradle bootRun
        } else {
            Write-Host "[ERROR] gradlew.bat or gradle not found!" -ForegroundColor Red
            exit 1
        }
    } else {
        $env:SPRING_PROFILES_ACTIVE = "dev"
        
        if (Test-Path ".\gradlew.bat") {
            .\gradlew.bat bootRun
        } elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
            gradle bootRun
        } else {
            Write-Host "[ERROR] gradlew.bat or gradle not found!" -ForegroundColor Red
            exit 1
        }
    }
} finally {
    # Cleanup on exit
    Stop-Job $WatcherJob.Id -ErrorAction SilentlyContinue
    Remove-Job $WatcherJob.Id -ErrorAction SilentlyContinue
}

