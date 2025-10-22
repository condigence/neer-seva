@echo off
REM run-app.bat - simple runner for NsUserServiceApplication
REM Usage:
REM   run-app.bat run        -> run via Maven plugin (spring-boot:run)
REM   run-app.bat build      -> build jar (mvnw clean package)
REM   run-app.bat start      -> run existing jar in target (no build)
REM   run-app.bat bg         -> open a new cmd window and run the jar there (no build)
REM   run-app.bat bg-run     -> open a new cmd window and run mvnw spring-boot:run there
REM   run-app.bat            -> if jar exists, start it; otherwise build then start
REM Optional: set SKIP_TESTS=1 to skip tests during the build.

:: Ensure script runs from its containing folder
pushd "%~dp0"
setlocal

echo ==================================================================
echo NsUserService - simple runner
echo ==================================================================

:: Resolve jar path (preferred name then any *-SNAPSHOT.jar)
set "DEFAULT_JAR=target\ns-user-service-0.0.1-SNAPSHOT.jar"
set "JAR="
if exist "%DEFAULT_JAR%" set "JAR=%DEFAULT_JAR%"
if "%JAR%"=="" (
    for /f "delims=" %%f in ('dir /b /a:-d target\*-SNAPSHOT.jar 2^>nul') do if "%JAR%"=="" set "JAR=target\%%f"
)

:: New: bg-run -> open a new cmd window and run the Maven spring-boot:run plugin (keeps the window open)
if "%1"=="bg-run" (
    echo Starting Maven spring-boot:run in a new cmd window...
    start "NS-USER-SERVICE (mvn-run)" cmd /k "cd /d "%~dp0" && mvnw.cmd -Dspring-boot.run.main-class=com.condigence.nsuserservice.NsUserServiceApplication spring-boot:run"
    endlocal
    popd
    exit /b 0
)

:: New: bg -> open a new cmd window and run the existing jar (keeps the window open)
if "%1"=="bg" (
    if "%JAR%"=="" (
        echo No jar found in target\. Please run "run-app.bat build" first.
        endlocal
        popd
        exit /b 1
    )
    echo Starting jar in new cmd window: %JAR%
    start "NS-USER-SERVICE" cmd /k "cd /d "%~dp0" && java -jar "%JAR%""
    endlocal
    popd
    exit /b 0
)

if "%1"=="run" (
    echo Running with Maven Spring Boot plugin (spring-boot:run)...
    if "%SKIP_TESTS%"=="1" (
        call mvnw.cmd -Dspring-boot.run.main-class=com.condigence.nsuserservice.NsUserServiceApplication spring-boot:run -DskipTests
    ) else (
        call mvnw.cmd -Dspring-boot.run.main-class=com.condigence.nsuserservice.NsUserServiceApplication spring-boot:run
    )
    set "EXITCODE=%ERRORLEVEL%"
    endlocal
    popd
    exit /b %EXITCODE%
)

if "%1"=="build" (
    echo Building artifact (clean package)...
    if "%SKIP_TESTS%"=="1" (
        call mvnw.cmd clean package -DskipTests
    ) else (
        call mvnw.cmd clean package
    )
    set "EXITCODE=%ERRORLEVEL%"
    endlocal
    popd
    exit /b %EXITCODE%
)

if "%1"=="start" (
    if "%JAR%"=="" (
        echo No jar found in target\. Please run "run-app.bat build" first.
        endlocal
        popd
        exit /b 1
    )
    echo Starting application: java -jar "%JAR%"
    java -jar "%JAR%"
    set "EXITCODE=%ERRORLEVEL%"
    endlocal
    popd
    exit /b %EXITCODE%
)

:: Default behaviour: start if jar exists, otherwise build then start
if not "%JAR%"=="" (
    echo Found jar: %JAR% - starting it now...
    java -jar "%JAR%"
    set "EXITCODE=%ERRORLEVEL%"
    endlocal
    popd
    exit /b %EXITCODE%
)

echo No jar found in target\. Building and starting...
if "%SKIP_TESTS%"=="1" (
    call mvnw.cmd clean package -DskipTests
) else (
    call mvnw.cmd clean package
)
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed with exit code %ERRORLEVEL%.
    endlocal
    popd
    pause
    exit /b %ERRORLEVEL%
)

:: resolve jar again after build
set "JAR=target\ns-user-service-0.0.1-SNAPSHOT.jar"
if not exist "%JAR%" (
    for /f "delims=" %%f in ('dir /b /a:-d target\*-SNAPSHOT.jar 2^>nul') do if "%JAR%"=="" set "JAR=target\%%f"
)
if not exist "%JAR%" (
    echo Build succeeded but no jar found in target\
    endlocal
    popd
    pause
    exit /b 1
)

echo Starting application: java -jar "%JAR%"
java -jar "%JAR%"

set "EXITCODE=%ERRORLEVEL%"
endlocal
popd
exit /b %EXITCODE%
