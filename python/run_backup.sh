#!/usr/bin/env bash
# Run from server: /opt/beeback/python/run_backup.sh
# Optional: create /opt/beeback/.env.backup with DB_PASSWORD=... and GMAIL_PASSWORD=...

set -e
BEEBACK_ROOT="${BEEBACK_ROOT:-/opt/beeback}"
cd "$BEEBACK_ROOT"
if [[ -f .env.backup ]]; then
  set -a
  source .env.backup
  set +a
fi
export BEEBACK_ROOT
export DB_PASSWORD="${DB_PASSWORD:-dorde}"
export DB_USER="${DB_USER:-root}"
python3 "$BEEBACK_ROOT/python/backup_beeback_db.py"
