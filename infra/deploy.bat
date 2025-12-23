@echo off
REM Windows batch script wrapper for deploy.py
REM Usage: deploy.bat [build|deploy]
REM This script automatically handles setup and runs the deployment

setlocal

REM Get script directory
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM Try python3 first, then python
python deploy.py %* 2>nul
if %errorlevel% neq 0 (
    python3 deploy.py %*
)

endlocal

