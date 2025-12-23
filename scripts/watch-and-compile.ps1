# Auto-compile script for hot reload (Windows PowerShell)
# Watches source files and automatically compiles when they change
# This enables DevTools hot reload without manual compilation

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir
Set-Location $ProjectRoot

Write-Host "[*] Watching source files for changes..." -ForegroundColor Cyan
Write-Host "    - Auto-compile on file save" -ForegroundColor Gray
Write-Host "    - DevTools will auto-restart app" -ForegroundColor Gray
Write-Host "    Press Ctrl+C to stop" -ForegroundColor Yellow
Write-Host ""

# Use Gradle continuous build to watch and compile
if (Test-Path ".\gradlew.bat") {
    .\gradlew.bat build --continuous -x test
} elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
    gradle build --continuous -x test
} else {
    Write-Host "[ERROR] gradlew.bat or gradle not found!" -ForegroundColor Red
    exit 1
}

