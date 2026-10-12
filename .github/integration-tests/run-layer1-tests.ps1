$ErrorActionPreference = "Stop"
Set-Location (Join-Path $PSScriptRoot "..\..")

docker info *> $null
if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker is not running. Start Docker and try again."
    exit 1
}

mvn verify -Dgroups=Layer1
$testExit = $LASTEXITCODE

if ($testExit -eq 0) {
    Write-Output "✅ Layer 1 integration tests PASSED"
} else {
    Write-Output "❌ Layer 1 integration tests FAILED"
}
exit $testExit
