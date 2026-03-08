# Copy config/firebase-credentials.json to the server and restart the app.
# Run from repo root: .\deploy\copy-firebase.ps1
# Requires: deploy/config with DEPLOY_HOST, DEPLOY_USER, DEPLOY_PATH

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
$localFile = Join-Path $projectRoot "config\firebase-credentials.json"

if (-not (Test-Path $localFile)) {
    Write-Error "Firebase credentials not found at $localFile. Add your Firebase service account JSON there (see deploy/README.md)."
    exit 1
}

$configPath = Join-Path $scriptDir "config"
if (Test-Path (Join-Path $scriptDir "config.local")) { $configPath = Join-Path $scriptDir "config.local" }
if (-not (Test-Path $configPath)) {
    Write-Error "Missing deploy/config. Copy deploy/config.example to deploy/config."
    exit 1
}

$lines = Get-Content $configPath | Where-Object { $_ -match "^\s*[A-Za-z_][A-Za-z0-9_]*=" }
$config = @{}
foreach ($line in $lines) {
    if ($line -match "^\s*([A-Za-z_][A-Za-z0-9_]*)=(.*)$") {
        $config[$matches[1]] = $matches[2].Trim().Trim('"').Trim("'")
    }
}
$host_name = $config["DEPLOY_HOST"]
$user = $config["DEPLOY_USER"]
$path = $config["DEPLOY_PATH"]
$sshKey = $config["SSH_KEY"]
if (-not $host_name -or -not $user -or -not $path) {
    Write-Error "Set DEPLOY_HOST, DEPLOY_USER, DEPLOY_PATH in deploy/config."
    exit 1
}

$remoteDir = $path -replace '\\', '/'
$remoteFile = "$remoteDir/config/firebase-credentials.json"
$sshTarget = "${user}@${host_name}"
$scpArgs = @()
if ($sshKey) { $scpArgs = @("-i", $sshKey) }
$pathEsc = $path -replace "'", "'\\''"

Write-Host "Ensuring config/ exists on server..." -ForegroundColor Cyan
& ssh @scpArgs $sshTarget "mkdir -p '$pathEsc/config'"
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host "Copying Firebase credentials to $sshTarget`:$remoteFile ..." -ForegroundColor Cyan
& scp @scpArgs $localFile "${sshTarget}:${remoteFile}"
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host "Restarting app container..." -ForegroundColor Yellow
& ssh @scpArgs $sshTarget "cd '$pathEsc' && docker compose restart app"
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host "Done. Check FCM init: ssh $sshTarget `"cd $pathEsc && docker compose logs app --tail 20`" | findstr FCM" -ForegroundColor Green
