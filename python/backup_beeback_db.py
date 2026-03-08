#!/usr/bin/env python3
"""Dump beeback_prod DB via docker compose, then optionally run send_beeback_db.py."""

import os
import subprocess
import sys
import time

# Config from env (defaults match docker-compose on server)
BEEBACK_ROOT = os.environ.get("BEEBACK_ROOT", "/opt/beeback")
BACKUP_CREATED_DIR = os.environ.get("BACKUP_CREATED_DIR", os.path.join(BEEBACK_ROOT, "backup", "created"))
DB_USER = os.environ.get("DB_USER", "root")
DB_PASSWORD = os.environ.get("DB_PASSWORD", "")
DATABASES = ("beeback_prod",)

if not DB_PASSWORD:
    print("DB_PASSWORD not set. Set it in env or in the runner script.", file=sys.stderr)
    sys.exit(1)

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
os.makedirs(BACKUP_CREATED_DIR, exist_ok=True)


def get_dump(database: str) -> None:
    filestamp = time.strftime("%Y-%m-%dT%H:%M:%S")
    out_file = os.path.join(BACKUP_CREATED_DIR, f"{database}_{filestamp}.sql")
    cmd = [
        "docker", "compose", "exec", "-T", "mysql",
        "mysqldump", "-u", DB_USER, f"-p{DB_PASSWORD}", database,
    ]
    try:
        with open(out_file, "w") as f:
            subprocess.run(cmd, cwd=BEEBACK_ROOT, stdout=f, check=True, timeout=300)
    except subprocess.CalledProcessError as e:
        print(f"mysqldump failed: {e}", file=sys.stderr)
        sys.exit(e.returncode)
    except FileNotFoundError:
        print("docker compose not found. Run from server where beeback compose is up.", file=sys.stderr)
        sys.exit(1)
    print(f"Database dumped to {out_file}")
    return out_file


def main() -> None:
    for database in DATABASES:
        get_dump(database)

    send_script = os.path.join(SCRIPT_DIR, "send_beeback_db.py")
    if not os.path.isfile(send_script):
        print("send_beeback_db.py not found; skipping send.")
        return
    env = os.environ.copy()
    env.setdefault("BACKUP_CREATED_DIR", BACKUP_CREATED_DIR)
    env.setdefault("BACKUP_PROCESSED_DIR", os.path.join(BEEBACK_ROOT, "backup", "processed"))
    result = subprocess.run([sys.executable, send_script], env=env, capture_output=True, text=True, cwd=BEEBACK_ROOT)
    if result.returncode == 0:
        print("DB sending script executed successfully.")
    else:
        print(f"DB sending script exited with {result.returncode}", file=sys.stderr)
        if result.stderr:
            print(result.stderr, file=sys.stderr)


if __name__ == "__main__":
    main()
