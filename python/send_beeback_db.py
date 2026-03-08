#!/usr/bin/env python3
"""Send backup files from BACKUP_CREATED_DIR by email, then move to BACKUP_PROCESSED_DIR. Needs GMAIL_PASSWORD."""

import glob
import os
import shutil
import smtplib
import sys
import time
from email.mime.application import MIMEApplication
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

created_dir = os.environ.get("BACKUP_CREATED_DIR", "/opt/beeback/backup/created")
processed_dir = os.environ.get("BACKUP_PROCESSED_DIR", "/opt/beeback/backup/processed")
password = os.environ.get("GMAIL_PASSWORD")
if not password:
    print("GMAIL_PASSWORD not set; skipping send.", file=sys.stderr)
    sys.exit(0)

os.makedirs(processed_dir, exist_ok=True)
pattern = os.path.join(created_dir, "beeback_prod*")
file_list = glob.glob(pattern)
if not file_list:
    print("No backup files to send.")
    sys.exit(0)

subject = "BB DB Backup - " + time.strftime("%Y-%m-%dT%H:%M:%S")
body = "This is the created Backup"
username = "backup.beeback@gmail.com"
recipient_email = os.environ.get("BACKUP_RECIPIENT_EMAIL", username)
server = "smtp.gmail.com"
port = 587

message = MIMEMultipart()
message["Subject"] = subject
message["From"] = username
message["To"] = recipient_email
message.attach(MIMEText(body))

for path in file_list:
    with open(path, "rb") as f:
        message.attach(MIMEApplication(f.read(), Name=os.path.basename(path)))

smtp = smtplib.SMTP(server, port)
smtp.starttls()
smtp.login(username, password)
smtp.sendmail(username, recipient_email, message.as_string())
smtp.quit()

for path in file_list:
    dest = os.path.join(processed_dir, os.path.basename(path))
    print(f"moved: {path} -> {dest}")
    shutil.move(path, dest)
