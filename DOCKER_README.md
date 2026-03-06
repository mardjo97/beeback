# Running beeback in Docker

From the **beeback** project root:

```bash
docker compose up --build
```

The app is bound on all interfaces so it is reachable at the server IP.

- **Backend API:** http://188.245.219.171:8080  
- **MySQL:** localhost:3308 (root password: dorde, database: beeback_prod)

First run may take a few minutes while the app image is built. The app waits for MySQL to be healthy before starting.

## Flutter app

In the Beekeeper Flutter app (Sync screen), set the backend URL to:

- **Production:** `http://188.245.219.171:8080`
- **Local Android emulator:** `http://10.0.2.2:8080`
- **Local iOS simulator / desktop / web:** `http://localhost:8080`

Log in with your backend user; the app will sync (including groups) when it starts and when it comes online.
