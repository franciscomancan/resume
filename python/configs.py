'''
The idea behind this script is to download the persisted config files from a given envs grid nodes, for quick inspection
(avoid having to login to different servers or finding passwords on prod, etc..)
'''

import paramiko
from scp import SCPClient
import os
import sys
import shutil     # to remove a non-empty dir (std lib cannot force)
import getpass

envNodes = {
    '-d': ['linuxghg001d','linuxghg002d'],
    '-t': ['linuxghg001t','linuxghg002t'],
    '-u': ['linuxghg001q','linuxghg002q','linuxghg003q','linuxghg004q','linuxmkghg001q','linuxmkghg002q','vwlghg003q','vwlghg004q'],
    '-p': ['linuxghg001p','linuxghg002p','linuxghg003p','linuxghg004p','linuxmkghg001p','linuxmkghg002p','vwlghg003p','vwlghg004p']
}
envs = { '-d': 'dev', '-t': 'test', '-u': 'uat', '-p': 'prod' }

remoteSourceDir = '/opt/deployment/config'
destinationRoot = 'C:/Users/samiam/development/anf/envConfigProps'

def download_configs(targetEnv):
    print(f"configs {targetEnv}")

    usr = getpass.getuser().lower()
    pw = getpass.getpass(f"pass ({usr}): ")
        
    env = envs[targetEnv]
    
    environmentDirRoot = f"{destinationRoot}/{env}"
    if os.path.isdir(environmentDirRoot):
        print("Cleaning configs ", environmentDirRoot)
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
            try:
                scp.get(remote_path=f"{remoteSourceDir}/application-{env}.properties", local_path=nodeDir)
                print(f"Downloaded app propes from {node}")
            except Exception as e:
                print(f"Issue retrieving active log file: {e}")

    scp.close()
    print("Process DONE")


def main():
    print(sys.argv[0:])
    print("Usage:  configs -d")
    if len(sys.argv) < 2:
        print("need an environment flag at least (i.e. -d, -t, -u, -p)")
    else:
        print(f"runing download_configs({sys.argv[1]})")
        download_configs(sys.argv[1])

if __name__ == "__main__":
    main()    
  


#copy_logs('-u','2022-07-01')