#!/usr/bin/env python
# coding: utf-8

# In[3]:


import os
import sys


# In[90]:


maxLinesThreshold = 999999999

def filter_path(finPath, topN = 20):
    #finPath = 'C:/Users/PTB56068/development/anf/revision/workstore/uows/GH2018-5252-sync.mongo.events.to.TD/20220804-perf.logs/perf.txt'
    #finPath = 'C:/Users/PTB56068/development/anf/logs/prod/mklghg002p/arc-grid.log.2022-06-29.2.txt'
    path = os.path.abspath(finPath)
    os.path.isdir(finPath)
    os.path.isfile(finPath)
    foutPath = f"{finPath}.worst{topN}.txt"
    totalLines = 0
    filterTimestamp = True
    filterUnderdow = True
                                                                    # make a first pass at input file and store uniq strings and count
    
    stats = {}
    rank = list()
    with open(finPath, "r") as input:
        while totalLines < maxLinesThreshold:
            line = input.readline()
            totalLines += 1
            if not line:
                break

            tokens = line.strip().split()
            if "ms" in tokens:
                idx = tokens.index("ms")
                stat = tokens[idx-1]
                if stat.isnumeric():
                    intStat = int(stat)
                    rank.append(intStat)
                    if not intStat in stats:
                        stats[intStat] = list()
                        
                    stats[intStat].append(line)
                        
                #print(stat)

        input.close()

    print(f"total distinct performance metrics = {len(stats)}")
    descStats = list(rank)
    descStats.sort(reverse=True)
    
    with open(foutPath, "w+") as output:
        for i in range(0,topN):
            n = descStats[i]
            output.write(f"runtime = {n}\n")
            nSamples = stats[n]
            for l in nSamples:
                output.write(f"\t{l}")
                
    output.close()
    print(f"Output written to {foutPath}")
                


# In[91]:


def main():
    #print(sys.argv[1:])    
    if len(sys.argv) < 2:
        print("Usage:  pr logFileToProcess.log 20")
        print("filter all performance stats from file, rank, and display top N worst cases (perf string in format of \"... 222054 ms\"\n")
        print("requires the filename to filter at minimum")        
    else:
        topNworst = sys.argv[2] if len(sys.argv) == 3 else int(20)
        if not topNworst.isnumeric():
            print(f"Processing top 20 worst case performance logs from {sys.argv[1]}")
            filter_path(sys.argv[1])
        else:
            print(f"Processing top {topNworst} worst case performance logs from {sys.argv[1]}")
            filter_path(sys.argv[1], int(topNworst))

if __name__ == "__main__":
    main()    


# In[ ]:




