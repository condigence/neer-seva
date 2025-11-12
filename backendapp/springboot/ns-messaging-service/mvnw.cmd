@echo off
REM Minimal mvnw.cmd wrapper. To fully enable the wrapper, generate the wrapper jar locally:
REM mvn -N io.takari:maven:wrapper
IF EXIST ".mvn\wrapper\maven-wrapper.jar" (
    java -jar ".mvn\wrapper\maven-wrapper.jar" %*
) ELSE (
    where mvn >nul 2>&1
    IF ERRORLEVEL 1 (
        echo Maven not found and maven-wrapper.jar is missing.
        echo Run "mvn -N io.takari:maven:wrapper" locally to generate the wrapper, or install Maven.
        exit /b 1
    ) ELSE (
        mvn %*
    )
)

