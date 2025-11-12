@echo off
REM run-mvn-and-start.cmd - builds the project and then runs Spring Boot (using mvnw.cmd if present)
REM Usage:
REM   run-mvn-and-start.cmd         -> builds and runs the app
REM   NO_RUN=1 run-mvn-and-start.cmd -> builds only (useful for CI/testing)

:: Determine mvn command (prefer mvnw.cmd in the repo root)
if exist "%~dp0mvnw.cmd" (
    set "MVN_CMD=%~dp0mvnw.cmd"
) else (
    set "MVN_CMD=mvn"
)
echo Using %MVN_CMD%
echo Running build: %MVN_CMD% -DskipTests clean install
call %MVN_CMD% -DskipTests clean install
if errorlevel 1 (
    echo.
    echo BUILD FAILED. See errors above.
    exit /b 1
)
echo.
echo BUILD SUCCEEDED.
if "%NO_RUN%"=="1" (
    echo NO_RUN=1 set; skipping spring-boot run.
    exit /b 0
)
echo Starting Spring Boot using %MVN_CMD% spring-boot:run
call %MVN_CMD% spring-boot:run

exit /b 0

