@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Script - DSA Logic Building Platform
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "LOCAL_MAVEN=C:\Users\carol cardoza\apache-maven-3.9.6\bin\mvn.cmd"
if exist "%LOCAL_MAVEN%" (
    call "%LOCAL_MAVEN%" %*
    exit /b %ERRORLEVEL%
)

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    call mvn %*
    exit /b %ERRORLEVEL%
)

echo [ERROR] Maven not found. Please install Maven or check path.
exit /b 1
