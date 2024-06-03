#!/usr/bin/env python
# coding: utf-8

"""
Util to download slices of logs for a closer look at the details.  This will
allow for use of other tools and also avoid the using certain clunky aggregators.
"""



# In[1]:


import paramiko
from scp import SCPClient
import gzip
import os
import sys
import shutil     # to remove a non-empty dir (std lib cannot force)
from datetime import date
import getpass


# In[2]:


envNodes = {
    '-d': ['lxlghg001d','lxlghg002d'],
    '-t': ['lxlghg001t','lxlghg002t'],
    '-u': ['lxlghg001q','lxlghg002q','lxlghg003q','lxlghg004q','mklghg001q','mklghg002q','vwlghg003q','vwlghg004q'],
    '-p': ['lxlghg001p','lxlghg002p','lxlghg003p','lxlghg004p','mklghg001p','mklghg002p','vwlghg003p','vwlghg004p']
}

envs = { '-d': 'dev', '-t': 'test', '-u': 'uat', '-p': 'prod' } 

envPatterns = {
    '-d': 'arc-grid',         # DEV configured differently (WTW?)
    '-t': 'arc-grid.log',
    '-u': 'arc-grid.log',
    '-p': 'arc-grid.log'
}
remoteSourceDir = '/opt/guidedhealth/logs'
destinationRoot = 'C:/Users/PTB56068/development/anf/logs'


# In[3]:


def copy_logs(targetEnv, targetDate = None):
    print(f"logs {targetEnv} {targetDate}")
    
    todaysLogs = False
    if targetDate is None or targetDate == '':
        targetDate = date.today()
        todaysLogs = True
        print("Fetching latest logs (today)")        
    
    logPattern = f"{envPatterns[targetEnv]}.{targetDate}*"
    usr = getpass.getuser().lower()
    pw = getpass.getpass(f"pass ({usr}): ")
        
    env = envs[targetEnv]
    
    environmentDirRoot = f"{destinationRoot}/{env}"
    if os.path.isdir(environmentDirRoot):
        print("Cleaning environment logs ", environmentDirRoot)
        shutil.rmtree(environmentDirRoot)

    print("Creating log output root ", environmentDirRoot)    
    os.mkdir(environmentDirRoot)

    nodes = envNodes[targetEnv]
    for node in nodes:
        nodeDir = f"{environmentDirRoot}/{node}"
        os.mkdir(nodeDir)

        client = paramiko.SSHClient()
        client.load_system_host_keys()
        client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        client.connect(node, 22, usr, pw)
                
        with SCPClient(client.get_transport(), sanitize=lambda x: x) as scp:
            if todaysLogs:
                try:
                    scp.get(remote_path=f"{remoteSourceDir}/arc-grid.log", local_path=nodeDir)
                    print("Downloaded current rolling log")
                except Exception as e:
                    print(f"Issue retrieving active log file: {e}")
            try:            
                scp.get(remote_path=f"{remoteSourceDir}/{logPattern}", local_path=nodeDir)
            except Exception as e:
                if not todaysLogs:
                    print(f"Issue log by date: {e}")

        archives = os.listdir(nodeDir)

        for file in archives:        
            if file.endswith("gz"):
                toFile = f"{file.replace('.gz','')}.txt"            
                absPathToZip = f"{nodeDir}/{file}"
                absPathToLog = f"{nodeDir}/{toFile}"
                #print(f"Checking file {absPathToZip} exist =  {os.path.isfile(absPathToZip)}")
                with open(absPathToZip, "rb") as archiveIn, open(absPathToLog, "w+", encoding='utf-8') as logOut:                
                    logContent = gzip.decompress(archiveIn.read()).decode()
                    print(f"Decompressing {len(logContent)} chars to {toFile}")
                    charsWritten = logOut.write(logContent)                
                    archiveIn.close()
                    logOut.flush()
                    logOut.close()
                    os.remove(absPathToZip)

    # Uploading the 'test' directory with its content in the
    # '/home/user/dump' remote directory
    # scp.put('test', recursive=True, remote_path='/home/user/dump')

    scp.close()
    print("Process DONE")


# In[4]:


def main():
    #print(sys.argv[1:])
    print("Usage:  logs -d 2022-04-05")
    if len(sys.argv) < 2:
        print("need an environment flag at least (i.e. -d, -t, -u, -p)")
    else:
        date = sys.argv[2] if len(sys.argv) == 3 else None
        copy_logs(sys.argv[1], date)

if __name__ == "__main__":
    main()    
  


# In[ ]:


#copy_logs('-u','2022-07-01')

