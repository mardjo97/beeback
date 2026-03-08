#!/usr/bin/env bash
# Beeback deploy script – run on server or from WSL/Mac/Linux
# Usage: ./deploy.sh [--build] [--skip-pull] [--setup] [--down]

set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Config: config.local overrides config
CONFIG_FILE="$SCRIPT_DIR/config"
[[ -f "$SCRIPT_DIR/config.local" ]] && CONFIG_FILE="$SCRIPT_DIR/config.local"
if [[ ! -f "$CONFIG_FILE" ]]; then
  echo "Missing deploy config. Copy deploy/config.example to deploy/config and set DEPLOY_HOST, DEPLOY_USER, DEPLOY_PATH." >&2
  exit 1
fi
source "$CONFIG_FILE"

: "${DEPLOY_HOST:?Set DEPLOY_HOST in config}"
: "${DEPLOY_USER:?Set DEPLOY_USER in config}"
: "${DEPLOY_PATH:?Set DEPLOY_PATH in config}"

SSH_TARGET="${DEPLOY_USER}@${DEPLOY_HOST}"
SSH_OPTS=()
[[ -n "${SSH_KEY:-}" ]] && SSH_OPTS=(-i "$SSH_KEY")

run_remote() {
  ssh "${SSH_OPTS[@]}" "$SSH_TARGET" "$@"
}

run_remote_script() {
  # stdin (e.g. heredoc) is forwarded to remote bash -s; strip \r for Windows CRLF
  sed 's/\r$//' | ssh "${SSH_OPTS[@]}" "$SSH_TARGET" "bash -s"
}

BUILD=false
SKIP_PULL=false
SETUP=false
DOWN=false
INSTALL_CRON=false
for arg in "$@"; do
  case "$arg" in
    --build)       BUILD=true ;;
    --skip-pull)   SKIP_PULL=true ;;
    --setup)       SETUP=true ;;
    --down)        DOWN=true ;;
    --install-cron) INSTALL_CRON=true ;;
  esac
done

echo "Deploy target: $SSH_TARGET ($DEPLOY_PATH)"

if [[ "$SETUP" == true ]]; then
  REPO_URL="$(git -C "$PROJECT_ROOT" remote get-url origin)" || { echo "Could not get git remote origin URL." >&2; exit 1; }
  PARENT_DIR="$(dirname "$DEPLOY_PATH")"
  echo "One-time setup: checking/installing Git and Docker on server, then cloning repo..."

  run_remote_script <<REMOTE_SCRIPT
set -e
export DEPLOY_PATH='${DEPLOY_PATH//\'/\'\\\'\'}'
export REPO_URL='${REPO_URL//\'/\'\\\'\'}'
export PARENT_DIR='${PARENT_DIR//\'/\'\\\'\'}'
[ "\$(id -u)" -eq 0 ] && SUDO='' || SUDO='sudo'

echo '=== Checking Git ==='
if command -v git >/dev/null 2>&1; then
  echo 'Git already installed.'
else
  echo 'Installing Git...'
  if [ -x /usr/bin/apt-get ]; then
    \$SUDO apt-get update -qq && \$SUDO apt-get install -y git
  elif [ -x /usr/bin/dnf ]; then
    \$SUDO dnf install -y git
  elif [ -x /usr/bin/yum ]; then
    \$SUDO yum install -y git
  elif [ -x /sbin/apk ]; then
    \$SUDO apk add --no-cache git
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
    curl -fsSL https://get.docker.com | \$SUDO sh
  fi
  echo 'Starting Docker...'
  \$SUDO systemctl start docker 2>/dev/null || \$SUDO service docker start 2>/dev/null || true
  \$SUDO systemctl enable docker 2>/dev/null || true
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
    \$SUDO apt-get update -qq && \$SUDO apt-get install -y docker-compose-plugin
  elif [ -x /usr/bin/dnf ]; then
    \$SUDO dnf install -y docker-compose-plugin
  elif [ -x /usr/bin/yum ]; then
    \$SUDO yum install -y docker-compose-plugin
  else
    echo 'Install docker-compose-plugin manually, then re-run setup.' >&2
    exit 1
  fi
fi

echo '=== Creating deploy path and cloning repo ==='
mkdir -p "\$PARENT_DIR"
if [ ! -d "\$DEPLOY_PATH" ]; then
  git clone "\$REPO_URL" "\$DEPLOY_PATH"
  echo "Cloned into \$DEPLOY_PATH"
else
  echo "Path \$DEPLOY_PATH already exists. Skipping clone."
fi
echo '=== Setup complete ==='
REMOTE_SCRIPT

  echo "Setup done. Run ./deploy/deploy.sh --build to deploy."
  exit 0
fi

if [[ "$DOWN" == true ]]; then
  echo "Stopping containers on server..."
  run_remote "cd '$DEPLOY_PATH' && docker compose down"
  echo "Done."
  exit 0
fi

if [[ "$INSTALL_CRON" == true ]]; then
  echo "Creating backup dirs and installing cron for Python backup..."
  run_remote "mkdir -p '$DEPLOY_PATH/backup/created' '$DEPLOY_PATH/backup/processed' && chmod +x '$DEPLOY_PATH/python/run_backup.sh' && \
    ( [ -f '$DEPLOY_PATH/.env.backup' ] || { cp '$DEPLOY_PATH/python/.env.backup.example' '$DEPLOY_PATH/.env.backup'; echo 'Created .env.backup from example'; } ) && \
    ( crontab -l 2>/dev/null | grep -v run_backup.sh || true; echo \"0 2 * * * $DEPLOY_PATH/python/run_backup.sh >> $DEPLOY_PATH/backup/backup.log 2>&1\" ) | crontab -"
  echo "Cron installed (daily 2 AM). Edit $DEPLOY_PATH/.env.backup on the server for DB_PASSWORD / GMAIL_PASSWORD."
  exit 0
fi

if [[ "$SKIP_PULL" == false ]]; then
  echo "Pulling latest on server..."
  run_remote "cd '$DEPLOY_PATH' && git pull"
fi

run_remote "cd '$DEPLOY_PATH' && docker compose rm -sf app 2>/dev/null; true"
if [[ "$BUILD" == true ]]; then
  echo "Building and starting containers (retrying on network errors)..."
  run_remote "cd '$DEPLOY_PATH' && for i in 1 2 3 4 5; do docker compose pull && docker compose up -d --build && exit 0; echo \"Attempt \$i failed, retry in 45s...\"; sleep 45; done; exit 1"
else
  echo "Starting containers (no rebuild)..."
  run_remote "cd '$DEPLOY_PATH' && for i in 1 2 3 4 5; do docker compose pull 2>/dev/null; docker compose up -d && exit 0; echo \"Attempt \$i failed, retry in 45s...\"; sleep 45; done; exit 1"
fi

echo "Deploy done. App: http://${DEPLOY_HOST}:8080"
