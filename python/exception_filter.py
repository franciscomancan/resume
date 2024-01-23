#!/usr/bin/env python
# coding: utf-8

# In[3]:


import os
import sys


# Takes a single input log file, groups found exceptions and counts and ranks them

# In[2]:


def filter_path(finPath):
    #finPath = 'C:/Users/simpatico/development/anf/logs/uat/search.txt'
    #finPath = 'C:/Users/simpatico/development/anf/logs/prod/mklghg002p/arc-grid.log.2022-06-29.2.txt'
    path = os.path.abspath(finPath)
    os.path.isdir(finPath)
    os.path.isfile(finPath)
    foutPath = f"{finPath}.exception.txt"
    totalLines = 0
    filterTimestamp = True
    filterUnderdow = True
                                                                    # make a first pass at input file and store uniq strings and count
    uniq = {}
    with open(finPath, "r") as input:
        while True:
            line = input.readline()
            totalLines += 1
            if not line:
                break

            line = line.strip()
            exc = line.split(" ")            
            if len(exc) > 0:
                for ele in exc:
                    if "Exception:" in ele:
                        subElements = ele.split(":")
                        for subElement in subElements:
                            if "Exception" in subElement:
                                uniqLineCount = 0
                                subElement = subElement.replace("[","")
                                subElement = subElement.replace("]","")                            
                                if subElement in uniq:
                                    uniqLineCount = uniq[subElement]

                                uniqLineCount += 1                            
                                uniq[subElement] = uniqLineCount

        input.close()

    print(f"total elements = {totalLines}")    
    written = 0

    #print(type(uniq))
    if len(uniq) > 1:
        print(f"unique elements = {len(uniq)}")
        print(f"unique ratio per file = {len(uniq)/totalLines}")
    
        sortedExceptions = sorted(uniq.items(), key=lambda x: x[1], reverse=True)
        with open(foutPath, "w+") as output:
            for ele in sortedExceptions:
                output.write(f"{ele[1]} => {ele[0]}\n")
                written += 1

            output.flush()
            output.close()

        print(f"Processing COMPLETE, lines written = {written}")
    else:
        print("NO EXCEPTION PATTERNS FOUND")


# In[ ]:


def main():
    #print(sys.argv[1:])
    
    if len(sys.argv) < 2:
        print("Usage:  fe someFile.log")
        print("Filter and rank exceptions in a given log file or dump of exceptions\n")
        
        print("**requires an input file to filter/rank exceptions")
    else:
        filter_path(sys.argv[1])

if __name__ == "__main__":
    main()    

