@echo off
REM Helper script to run the Maven tests and provide a friendly error if mvn is missing.
SET PROJECT_DIR=%~dp0
ECHO Project directory: %PROJECT_DIR%

REM Check for mvn on PATH
where mvn >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO Maven (mvn) not found on PATH. Please install Maven and add it to your PATH.
    ECHO https://maven.apache.org/install.html
    EXIT /B 1
)

cd /d "%PROJECT_DIR%"
ECHO Running: mvn test
mvn test
EXIT /B %ERRORLEVEL%
