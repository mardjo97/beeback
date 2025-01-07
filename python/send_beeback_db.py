import smtplib
import glob
import os
import shutil
import time
# MIMEMultipart send emails with both text content and attachments.
from email.mime.multipart import MIMEMultipart
# MIMEText for creating body of the email message.
from email.mime.text import MIMEText
# MIMEApplication attaching application-specific data (like CSV files) to email messages.
from email.mime.application import MIMEApplication

subject = "BB DB Backup - "+time.strftime('%Y-%m-%dT%H:%M:%S')
body = "This is the created Backup"
username = "backup.beeback@gmail.com"
recipient_email = "backup.beeback@gmail.com"
#password = os.getenv("GMAIL_PASSWORD")
#print(password)
#password = "pwd"
server = 'smtp.gmail.com'
port = 587

move_to_dest_path = '/backup/processed'

# MIMEMultipart() creates a container for an email message that can hold
# different parts, like text and attachments and in next line we are
# attaching different parts to email container like subject and others.
message = MIMEMultipart()
message['Subject'] = subject
message['From'] = username
message['To'] = recipient_email
body_part = MIMEText(body)
message.attach(body_part)

pattern = os.path.join('/backup/created', 'beeback_prod*')
file_list = glob.glob(pattern)

# section 1 to attach file
for path in file_list:
    with open(path,'rb') as file:
        # Attach the file with filename to the email
        #print(path)
        #print(file.read())
        message.attach(MIMEApplication(file.read(), Name=os.path.basename(path)))

# secction 2 for sending email
#with smtplib.SMTP_SSL(smtp_server, smtp_port) as server:
#   server.starttls()
#   server.login(sender_email, sender_password)
#   server.sendmail(sender_email, recipient_email, message.as_string())

smtp = smtplib.SMTP(server, port)
smtp.starttls()
smtp.login(username, password)
smtp.sendmail(username, recipient_email, message.as_string())
smtp.quit()

#time.sleep(10)

for path in file_list:
    dest_path = os.path.join(move_to_dest_path, os.path.basename(path))
    print("moved: :"+path+" -> "+dest_path)
    shutil.move(path, dest_path)
