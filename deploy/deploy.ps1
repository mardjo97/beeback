# Beeback deploy script – sync and run docker compose on server 188.245.219.171
# Usage: .\deploy.ps1 [--build] [--skip-pull] [--setup] [--down]
# With -Setup: installs git and Docker on server if missing, then clones repo.

param(
    [switch]$Build,       # run docker compose up -d --build
    [switch]$SkipPull,    # skip git pull on server
    [switch]$Setup,       # one-time: create DEPLOY_PATH on server and clone repo
    [switch]$Down,        # run docker compose down on server
    [switch]$InstallCron  # create backup dirs + crontab for python/run_backup.sh (daily 2 AM)
)

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir

# Load config: config.local overrides config
$configPath = Join-Path $scriptDir "config"
$configLocal = Join-Path $scriptDir "config.local"
if (Test-Path $configLocal) { $configPath = $configLocal }
if (-not (Test-Path $configPath)) {
    Write-Error "Missing deploy config. Copy deploy\config.example to deploy\config and set DEPLOY_HOST, DEPLOY_USER, DEPLOY_PATH."
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
    Write-Error "Config must set DEPLOY_HOST, DEPLOY_USER, DEPLOY_PATH."
    exit 1
}

$sshTarget = "${user}@${host_name}"
$sshOpts = @()
if ($sshKey) { $sshOpts = @("-i", $sshKey) }

function Invoke-Remote {
    param([string]$Command)
    $argList = $sshOpts + @("$sshTarget", $Command)
    & ssh $argList
    if ($LASTEXITCODE -ne 0) { throw "ssh failed: $Command" }
}

function Invoke-RemoteScript {
    param([string]$Script)
    # Use Unix (LF) line endings so remote bash does not see \r
    $Script = $Script -replace "`r`n", "`n" -replace "`r", "`n"
    $sshArgs = @()
    if ($sshKey) { $sshArgs += @("-i", $sshKey) }
    $sshArgs += @("$sshTarget", "bash -s")
    $Script | & ssh $sshArgs
    if ($LASTEXITCODE -ne 0) { throw "Remote script failed." }
}

Write-Host "Deploy target: $sshTarget ($path)" -ForegroundColor Cyan

if ($Setup) {
    $repoUrl = ( & git -C $projectRoot remote get-url origin 2>$null )
    if (-not $repoUrl) {
        Write-Error "Could not get git remote origin URL. Run from repo root and ensure origin is set."
        exit 1
    }
    $parentDir = Split-Path -Parent $path
    $pathEscaped = $path -replace "'", "'\\''"
    $urlEscaped = $repoUrl.Trim() -replace "'", "'\\''"

    $bootstrap = @"
set -e
export DEPLOY_PATH='$pathEscaped'
export REPO_URL='$urlEscaped'
export PARENT_DIR='$($parentDir -replace "'", "'\\''")'
[ \"`$(id -u)\" -eq 0 ] && SUDO='' || SUDO='sudo'

echo '=== Checking Git ==='
if command -v git >/dev/null 2>&1; then
  echo 'Git already installed.'
else
  echo 'Installing Git...'
  if [ -x /usr/bin/apt-get ]; then
    `$SUDO apt-get update -qq && `$SUDO apt-get install -y git
  elif [ -x /usr/bin/dnf ]; then
    `$SUDO dnf install -y git
  elif [ -x /usr/bin/yum ]; then
    `$SUDO yum install -y git
  elif [ -x /sbin/apk ]; then
    `$SUDO apk add --no-cache git
  else
    echo 'Cannot install Git: no supported package manager (apt/dnf/yum/apk).' >&2
    exit 1
  fi
fi

echo '=== Checking Docker ==='
if command -v docker >/dev/null 2>&1 && docker info >/dev/null 2>&1; then
  echo 'Docker already installed and running.'
else
  if ! command -v docker >/dev/null 2>&1; then
    echo 'Installing Docker...'
    curl -fsSL https://get.docker.com | `$SUDO sh
  fi
  echo 'Starting Docker...'
  `$SUDO systemctl start docker 2>/dev/null || `$SUDO service docker start 2>/dev/null || true
  `$SUDO systemctl enable docker 2>/dev/null || true
  sleep 2
  if ! docker info >/dev/null 2>&1; then
    echo 'Docker failed to start. Check: systemctl status docker' >&2
    exit 1
  fi
fi

echo '=== Checking Docker Compose ==='
if docker compose version >/dev/null 2>&1; then
  echo 'Docker Compose (plugin) OK.'
elif command -v docker-compose >/dev/null 2>&1; then
  echo 'Docker Compose (standalone) OK.'
else
  echo 'Installing Docker Compose plugin...'
  if [ -x /usr/bin/apt-get ]; then
    `$SUDO apt-get update -qq && `$SUDO apt-get install -y docker-compose-plugin
  elif [ -x /usr/bin/dnf ]; then
    `$SUDO dnf install -y docker-compose-plugin
  elif [ -x /usr/bin/yum ]; then
    `$SUDO yum install -y docker-compose-plugin
  else
    echo 'Install docker-compose-plugin manually, then re-run setup.' >&2
    exit 1
  fi
fi

echo '=== Creating deploy path and cloning repo ==='
mkdir -p "`$PARENT_DIR"
if [ ! -d "`$DEPLOY_PATH" ]; then
  git clone "`$REPO_URL" "`$DEPLOY_PATH"
  echo "Cloned into `$DEPLOY_PATH"
else
  echo "Path `$DEPLOY_PATH already exists. Skipping clone."
fi
echo '=== Setup complete ==='
"@
    Write-Host "One-time setup: checking/installing Git and Docker on server, then cloning repo..." -ForegroundColor Yellow
    Invoke-RemoteScript $bootstrap
    Write-Host "Setup done. Run .\deploy\deploy.ps1 -Build to deploy." -ForegroundColor Green
    exit 0
}

if ($Down) {
    Write-Host "Stopping containers on server..." -ForegroundColor Yellow
    Invoke-Remote "cd '$path' && docker compose down"
    Write-Host "Done." -ForegroundColor Green
    exit 0
}

if ($InstallCron) {
    $pathEsc = $path -replace "'", "'\\''"
    Write-Host "Creating backup dirs and installing cron for Python backup..." -ForegroundColor Yellow
    $cronCmd = @"
mkdir -p '$pathEsc/backup/created' '$pathEsc/backup/processed' && chmod +x '$pathEsc/python/run_backup.sh' && \
if [ ! -f '$pathEsc/.env.backup' ]; then cp '$pathEsc/python/.env.backup.example' '$pathEsc/.env.backup'; echo 'Created .env.backup from example; edit it to set DB_PASSWORD (and GMAIL_PASSWORD if you use email backup).'; fi && \
(crontab -l 2>/dev/null | grep -v run_backup.sh || true; echo "0 2 * * * $pathEsc/python/run_backup.sh >> $pathEsc/backup/backup.log 2>&1") | crontab -
"@
    Invoke-Remote $cronCmd
    Write-Host "Cron installed (daily 2 AM). Backup dirs: $path/backup/created, $path/backup/processed." -ForegroundColor Green
    Write-Host "Edit $path/.env.backup on the server to set DB_PASSWORD (and GMAIL_PASSWORD for email)." -ForegroundColor Cyan
    exit 0
}

if (-not $SkipPull) {
    Write-Host "Pulling latest on server..." -ForegroundColor Yellow
    Invoke-Remote "cd '$path' && git pull"
}

$pathEscapedForRemote = $path -replace "'", "'\\''"
if ($Build) {
    Write-Host "Building and starting containers (retrying on network errors)..." -ForegroundColor Yellow
    Invoke-Remote "cd '$pathEscapedForRemote' && docker compose rm -sf app 2>/dev/null; true"
    $retryCmd = "cd '$pathEscapedForRemote' && for i in 1 2 3 4 5; do docker compose pull && docker compose up -d --build && exit 0; echo `"Attempt `$i failed, retry in 45s...`"; sleep 45; done; exit 1"
    Invoke-Remote $retryCmd
} else {
    Write-Host "Starting containers (no rebuild)..." -ForegroundColor Yellow
    Invoke-Remote "cd '$pathEscapedForRemote' && docker compose rm -sf app 2>/dev/null; true"
    $retryCmd = "cd '$pathEscapedForRemote' && for i in 1 2 3 4 5; do docker compose pull 2>/dev/null; docker compose up -d && exit 0; echo `"Attempt `$i failed, retry in 45s...`"; sleep 45; done; exit 1"
    Invoke-Remote $retryCmd
}

Write-Host "Deploy done. App: http://${host_name}:8080" -ForegroundColor Green
