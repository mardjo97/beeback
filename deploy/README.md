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
