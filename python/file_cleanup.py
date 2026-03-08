#!/usr/bin/env python3
"""Move backup files from created to processed dir. Uses env for paths."""

import os
import glob
import shutil

created_dir = os.environ.get("BACKUP_CREATED_DIR", "/opt/beeback/backup/created")
processed_dir = os.environ.get("BACKUP_PROCESSED_DIR", "/opt/beeback/backup/processed")

os.makedirs(processed_dir, exist_ok=True)
pattern = os.path.join(created_dir, "beeback_prod*")
file_list = glob.glob(pattern)

for path in file_list:
    dest_path = os.path.join(processed_dir, os.path.basename(path))
    print(f"moved: {path} -> {dest_path}")
    shutil.move(path, dest_path)
