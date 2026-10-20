@REM ----------------------------------------------------------------------------
@REM Maven Wrapper script for Windows (cmd.exe)
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "DIR=%~dp0"
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    mvn %*
    exit /b %ERRORLEVEL%
)

echo [INFO] Apache Maven not detected in system PATH.
echo [INFO] Launching via system maven command...
mvn %*
exit /b %ERRORLEVEL%
