@echo off
REM Windows batch script wrapper for start-dev-watcher.ps1
REM Usage: start-dev-watcher.bat

powershell -ExecutionPolicy Bypass -File "%~dp0start-dev-watcher.ps1"

