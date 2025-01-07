import os
import glob
import shutil

move_to_dest_path = '/backup/processed'

pattern = os.path.join('/backup/created', 'beeback_prod*')
file_list = glob.glob(pattern)

for path in file_list:
    dest_path = os.path.join(move_to_dest_path, os.path.basename(path))
    print("moved: :"+path+" -> "+dest_path)
    shutil.move(path, dest_path)
