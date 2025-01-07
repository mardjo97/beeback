#!/usr/local/bin/python3

import configparser
import os
import time
import getpass
import subprocess
HOST='localhost'
PORT='3306'
DB_USER='dorde'
#DB_PASS='pwd'
#DB_PASS=os.getenv("DB_PASSWORD")
PATH='/backup/created/'
# if using one database... ('database1',)
databases=('beeback_prod',)

def get_dump(database):
    filestamp = time.strftime('%Y-%m-%dT%H:%M:%S')
    os.popen("docker exec -i my-sql mysqldump -u %s -p%s %s > %s.sql" % (DB_USER,DB_PASS,database,PATH+database+"_"+filestamp))
    print("\n|| Database dumped to "+database+"_"+filestamp+".sql || ")
    #os.system('python3 /backup/send_beeback_db.py')
    time.sleep(5)
    result = subprocess.run(['python3', '/backup/send_beeback_db.py'], capture_output=True)
    if result.returncode == 0:
        print("DB sending script executed successfully.")
    else:
        print(f"DB sending script failed with exit code {result.returncode}")
        print(f"Error: {result.stderr.decode()}") 

if __name__=="__main__":
    for database in databases:
        get_dump(database)
