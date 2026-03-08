# Beeback deployment (188.245.219.171)

Quick deploy to the server without manual SSH and copy-paste.

## One-time setup on your machine

1. Copy the config and edit it:
   ```bash
   cp deploy/config.example deploy/config
   ```
   Set `DEPLOY_HOST`, `DEPLOY_USER`, `DEPLOY_PATH`. Optionally `SSH_KEY` if not using default.

2. Ensure you can SSH to the server:
   ```bash
   ssh root@188.245.219.171
   ```

## One-time setup (fully automated)

From your machine, run the deploy script with **`-Setup`** once. It will SSH to the server and:

1. **Install Git** if missing (apt, dnf/yum, or apk)
2. **Install Docker** if missing (via [get.docker.com](https://get.docker.com)), then start and enable it
3. **Install Docker Compose** (plugin) if missing
4. **Create `/opt/beeback`** and **clone this repo** (using your local `git remote origin` URL)

No need to install anything on the server by hand; the script does it.

**PowerShell:**
```powershell
.\deploy\deploy.ps1 -Setup
```

**Bash:**
```bash
./deploy/deploy.sh --setup
```

**Supported server OS:** Debian/Ubuntu, RHEL/CentOS/Fedora, Alpine (with root or sudo).

## Deploy (from your machine)

**PowerShell (Windows):**
```powershell
cd c:\Users\djord\projects\beeback
.\deploy\deploy.ps1 -Build
```
- `-Setup` – **run once** to create `/opt/beeback` on the server and clone the repo (no deploy).
- `-Build` – run `docker compose up -d --build` on the server (rebuild images).
- Omit `-Build` – only `docker compose up -d` (no rebuild).
- `-SkipPull` – skip `git pull` on the server.
- `-Down` – run `docker compose down` on the server.
- `-InstallCron` – create backup dirs and install **daily 2 AM** cron for the Python DB backup script (see below).

**Bash (WSL / Mac / Linux):**
```bash
cd /path/to/beeback
chmod +x deploy/deploy.sh
./deploy/deploy.sh --build
```

## Quick workflow

1. Push your changes: `git push`
2. Deploy and rebuild on server: `.\deploy\deploy.ps1 -Build`  
   → Server runs `git pull` then `docker compose up -d --build`.
3. App: **http://188.245.219.171:8080**

To only restart containers without pulling or rebuilding:  
`.\deploy\deploy.ps1 -SkipPull`

### Connection refused on port 8080

If you get **connection refused** when opening http://188.245.219.171:8080:

1. **Check containers on the server**
   ```bash
   ssh root@188.245.219.171 "cd /opt/beeback && docker compose ps"
   ```
   Both `app` and `mysql` should be `Up`. If `app` is `Exit` or missing, check logs:
   ```bash
   ssh root@188.245.219.171 "cd /opt/beeback && docker compose logs app --tail 80"
   ```

2. **Open port 8080 in the server firewall** (if enabled)
   - **ufw (Ubuntu/Debian):** `sudo ufw allow 8080/tcp && sudo ufw reload`
   - **firewalld:** `sudo firewall-cmd --permanent --add-port=8080/tcp && sudo firewall-cmd --reload`
   - **Cloud (e.g. Hetzner/AWS):** In the control panel, add an inbound rule for TCP port 8080 from 0.0.0.0/0 or your IP.

3. **Redeploy** so the app binds on all interfaces (we use `0.0.0.0:8080:8080` in compose):
   ```powershell
   .\deploy\deploy.ps1 -Build
   ```

### Firebase (FCM) on the server

The app can send reminder push notifications via Firebase Cloud Messaging. The JSON key file is **not** in Git (it’s in `.gitignore`). To enable FCM on the server:

1. **Copy your Firebase credentials file to the server** (same file you use locally, e.g. `config/firebase-credentials.json`):
   ```powershell
   scp config\firebase-credentials.json root@188.245.219.171:/opt/beeback/config/
   ```
   Or from Bash: `scp config/firebase-credentials.json root@188.245.219.171:/opt/beeback/config/`

2. **Restart the app** so it picks up the mounted file:
   ```powershell
   .\deploy\deploy.ps1 -SkipPull
   ```
   Or on the server: `cd /opt/beeback && docker compose restart app`

3. **Check that it’s used**: app logs should show `FCM: initialized from /app/config/firebase-credentials.json` (not “no credentials path set” or “could not load credentials”):
   ```bash
   ssh root@188.245.219.171 "cd /opt/beeback && docker compose logs app --tail 30" | findstr FCM
   ```

The deploy script ensures `config/` exists on the server. You can copy the file and restart in one go from your machine:

```powershell
.\deploy\copy-firebase.ps1
```

(This requires `config\firebase-credentials.json` to exist locally and uses `deploy\config` for host/user/path.)

---

## Python scripts (backup and cron)

The repo includes Python scripts in **`python/`**:

- **`backup_beeback_db.py`** – Dumps `beeback_prod` via `docker compose exec mysql mysqldump`, then optionally runs **`send_beeback_db.py`** to email the backup.
- **`send_beeback_db.py`** – Sends backup files from `BACKUP_CREATED_DIR` by email (Gmail), then moves them to `BACKUP_PROCESSED_DIR`. Needs `GMAIL_PASSWORD` (and optionally `BACKUP_RECIPIENT_EMAIL`).
- **`file_cleanup.py`** – Moves backup files from created to processed dir (no email).

They are deployed with the repo (in `/opt/beeback/python/` after `git pull`). To **activate** the backup on a schedule:

1. **One-time:** install cron and backup dirs from your machine:
   ```powershell
   .\deploy\deploy.ps1 -InstallCron
   ```
   This creates `/opt/beeback/backup/created`, `/opt/beeback/backup/processed`, makes `python/run_backup.sh` executable, copies `python/.env.backup.example` to `/opt/beeback/.env.backup` if missing, and adds a crontab entry to run the backup **daily at 2 AM**.

2. **On the server:** ensure `/opt/beeback/.env.backup` has at least:
   - `DB_PASSWORD=dorde` (must match MySQL root password in docker-compose).
   - Optionally `GMAIL_PASSWORD=...` and `BACKUP_RECIPIENT_EMAIL=...` if you use email backup.

3. **Manual run on server:**  
   `cd /opt/beeback && ./python/run_backup.sh`

All paths and credentials are controlled by env (see `python/.env.backup.example` and `BEEBACK_ROOT`, `BACKUP_CREATED_DIR`, `BACKUP_PROCESSED_DIR`, `DB_USER`, `DB_PASSWORD`, `GMAIL_PASSWORD`).
