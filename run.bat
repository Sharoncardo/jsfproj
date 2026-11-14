@echo off
title DSA Logic Building Platform - Sharon Cardoza Module
echo ========================================================
echo  DSA Logic Building Platform
echo  Core Problem Solving & Progressive Hint Engine
echo  Sharon Cardoza Module
echo ========================================================
echo Starting application on http://localhost:8080 ...
echo.

cd /d "%~dp0"

REM If JAR exists in target, run directly for instant launch
if exist "target\dsa-logic-platform-1.0.0-SNAPSHOT.jar" (
    echo Running compiled JAR...
    start "" http://localhost:8080/index.html
    java -jar target\dsa-logic-platform-1.0.0-SNAPSHOT.jar
    goto :eof
)

REM Otherwise run via Maven
echo Building and running via Maven...
start "" http://localhost:8080/index.html
call mvnw.cmd spring-boot:run
pause
