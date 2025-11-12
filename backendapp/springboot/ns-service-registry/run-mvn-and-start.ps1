<#
runs the maven build and then starts spring-boot:run
Usage:
  .\run-mvn-and-start.ps1            # build and run
  $env:NO_RUN=1; .\run-mvn-and-start.ps1  # build only
#>
param()

# locate mvnw in repo root
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$mvnw = Join-Path $scriptDir 'mvnw.cmd'
if (Test-Path $mvnw) {
    $mvnCmd = $mvnw
} else {
    $mvnCmd = 'mvn'
}
Write-Host "Using $mvnCmd"

# Run build
Write-Host "Running: $mvnCmd -DskipTests clean install"
$proc = Start-Process -FilePath $mvnCmd -ArgumentList '-DskipTests','clean','install' -NoNewWindow -Wait -PassThru
if ($proc.ExitCode -ne 0) {
    Write-Error "Build failed (exit code $($proc.ExitCode)). Aborting."
    exit $proc.ExitCode
}
Write-Host "BUILD SUCCEEDED"

if ($env:NO_RUN -eq '1') {
    Write-Host "NO_RUN=1 set; skipping spring-boot run."
    exit 0
}

Write-Host "Starting Spring Boot: $mvnCmd spring-boot:run"
Start-Process -FilePath $mvnCmd -ArgumentList 'spring-boot:run' -NoNewWindow -Wait

