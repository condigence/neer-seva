@echo off
REM run-springboot.bat - build and run NsUserServiceApplication simply on Windows
REM Usage:
REM   run-springboot.bat build-and-run   -> mvnw.cmd clean install then start the jar in a new cmd window (keeps window open)
REM   run-springboot.bat run            -> run mvnw.cmd spring-boot:run in current window
REM   run-springboot.bat bg-run         -> open a new cmd window and run mvnw.cmd spring-boot:run there (keeps window open)
REM   run-springboot.bat bg-start       -> open a new cmd window and run the built jar there (keeps window open)

pushd "%~dp0"
setlocal

n:: Default jar name (adjust if artifactId/version changes)
set "JAR=target\ns-user-service-0.0.1-SNAPSHOT.jar"

nif "%1"=="build-and-run" (
    echo Building project (mvnw clean install)...
    call mvnw.cmd clean install -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed with exit code %ERRORLEVEL%.
        endlocal
        popd
        exit /b %ERRORLEVEL%
    )
    if exist "%JAR%" (
        echo Launching jar in a new cmd window: %JAR%
        start "NsUserService" cmd /k "cd /d "%~dp0" && java -jar "%JAR%""
        endlocal
        popd
        exit /b 0
    ) else (
        echo Build succeeded but jar not found: %JAR%
        endlocal
        popd
        exit /b 1
    )
)

nif "%1"=="run" (
    echo Running Spring Boot using Maven plugin (spring-boot:run)...
    call mvnw.cmd -Dspring-boot.run.main-class=com.condigence.nsuserservice.NsUserServiceApplication spring-boot:run
    set "EXITCODE=%ERRORLEVEL%"
    endlocal
    popd
    exit /b %EXITCODE%
)

nif "%1"=="bg-run" (
    echo Starting Maven spring-boot:run in a new cmd window...
    start "NsUserService (mvn-run)" cmd /k "cd /d "%~dp0" && mvnw.cmd -Dspring-boot.run.main-class=com.condigence.nsuserservice.NsUserServiceApplication spring-boot:run"
    endlocal
    popd
    exit /b 0
)

nif "%1"=="bg-start" (
    if not exist "%JAR%" (
        echo Jar not found: %JAR% - build first using "build-and-run" or "mvnw clean install".
        endlocal
        popd
        exit /b 1
    )
    echo Starting jar in a new cmd window: %JAR%
    start "NsUserService" cmd /k "cd /d "%~dp0" && java -jar "%JAR%""
    endlocal
    popd
    exit /b 0
)

n:: default: build-and-run
necho No argument provided - running default: build-and-run
ncall "%~dp0run-springboot.bat" build-and-run
endlocal
popd
exit /b 0

