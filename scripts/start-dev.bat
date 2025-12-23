@echo off
REM Windows Batch script wrapper for PowerShell script
REM Usage: start-dev.bat
REM This automatically runs the PowerShell script with proper execution policy

setlocal

REM Get script directory
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%\.."

REM Check if PowerShell is available
where powershell >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ PowerShell not found!
    echo Please install PowerShell or use Docker Desktop directly
    exit /b 1
)

REM Run PowerShell script with execution policy bypass
powershell -ExecutionPolicy Bypass -NoProfile -File "%~dp0start-dev.ps1"

endlocal

