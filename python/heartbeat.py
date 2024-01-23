#!/usr/bin/env python
# coding: utf-8

# Heartbeat for spring boot applications using their actuator endpoints

# In[3]:


import requests;

requests.packages.urllib3.disable_warnings()

print("Grid node heartbeat")

headers = {'iv-user': 'health-inspector', 'iv-groups': 'ARC-SysAdm'}

hosts = {
"dev1":"linuxghg001d",
"dev2":"linuxghg001d",
"test1":"linuxghg001t",
"test2":"linuxghg001t",
"uat1":"linuxghg001q",
"uat2":"linuxghg002q",
"uat3":"linuxghg003q",
"uat4":"linuxghg004q",
"uat5":"minuxghg001q",
"uat6":"minuxghg002q",
"uat7":"westghg003q",
"uat8":"westghg004q",
"prod1":"linuxghg001p",
"prod2":"linuxghg002p",
"prod3":"linuxghg003p",
"prod4":"linuxghg004p",
"prod5":"minuxghg001p",
"prod6":"minuxghg002p",
"prod7":"westghg003p",
"prod8":"westghg004p"
}

for env, loc in hosts.items():
    eloc = f"https://{loc}:8888/actuator/health/"

    try:
        resp = requests.get(eloc, verify=False, headers=headers, timeout=5)        
        if resp.json()['status'] == 'UP':
            print(f"{env} - green  \t\t ({loc})")
            
    except Exception as e:
        print(f"{env} NOT RESPONDING  ->  {str(e)}")
        


# In[ ]:




