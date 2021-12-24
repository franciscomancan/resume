#!/usr/bin/env python
# coding: utf-8

# In[24]:


from pandas import ExcelFile
import os

# https://pymotw.com/2/xml/etree/ElementTree/create.html


# In[25]:


states = ['AZ','CO','NM','UT']
headers = ['code','ext','rate','mp','hg']
old_rates = {}
new_rates = {}
changed_codes = {}
change_map = {}        # {az -> {'3303':'B', '8810':'C'}}
log = ['code,state,action,hazard_group,public_id,phrase']

for state in states:
    ef = ExcelFile(f'{state}_old.xlsx')
    df = ef.parse(ef.sheet_names[0]).iloc[12:]
    df.columns = headers
    old_rates[state] = df[["code","hg"]]
    #print(old_rates[state].head(), "\n-------")
    
for state in states:
    ef = ExcelFile(f'{state}_new.xlsx')
    df = ef.parse(ef.sheet_names[0]).iloc[12:]
    df.columns = headers
    new_rates[state] = df[["code","hg"]]
    #print(new_rates[state].head(), "\n-------")


print('old sets', len(old_rates))    
print('new sets', len(new_rates))
#print('\n\n', old_rates[states[0]].head(19))
#print('\n\n', new_rates[states[0]].head(19))


# In[26]:


for state in states:
    print(f'---------------------------------------\nEVALUATING STATE = {state}')
    old = set(old_rates[state].itertuples(index=False, name=None))
    new = set(new_rates[state].itertuples(index=False, name=None))
    print(f'old codes = {len(old)}, new code = {len(new)}')
    diff = list(new - old)
    changed_codes[state] = sorted(diff)
    print(f'found differences in {state}', len(diff), '\n')
    print(changed_codes[state][0], '...')
    
    
with open('changed_hazard_groups.txt', mode='w') as groupChanges:       
    for k, v in changed_codes.items():
        groupChanges.write(f'Changed Hazard Groups for {k} \n ____________________________________________________ \n')
        for row in changed_codes[k]:
            groupChanges.write(f'{str(row)} \n')
            
    print('\n\n  CHANGED CODE-GROUPS WRITTEN TO changed_hazard_groups.txt')


# In[27]:


#  from xml.dom import minidom     VALUES NOT WORKING WITH THIS LIBRARY, DISPLAYING AS None => etree much easier
#from xml.etree.ElementTree import Element, SubElement, Comment, tostring
import xml.etree.ElementTree as et

NEW_EXPIRATION_DATE = '2022-01-01 00:00:00.000'
NEW_EFFECTIVE_DATE = '2022-01-01 00:00:00.000'
PUBLIC_ID_PREFIX = 'ncci2022'
PUBLIC_ID_COUNTER = 1000


# In[28]:


def expireHazardGroup(el, t):
    #print('expireHazardGroup', t)    
    ed = el.find('ExpirationDate')    
    if ed is None:
        ed = et.SubElement(el, "ExpirationDate")
        ed.text = NEW_EXPIRATION_DATE
        #el.append(ed)
    
    phrase = el.find('Classification')
    pid = el.get('public-id')
    log.append(f'"{t[0]}",{t[1]},expire,{t[2]},{pid},{phrase.text}')

''' 
After trying both et.ElementTree(other).getroot() and et.ElementTree.Element(other),
having to manually copy each element.  Other trys resulted in just a shallow copy or
a copy of a reference, where changes are affecting both orig and copy elements.
'''
def copyElement(other):
    e = et.Element('WC7ClassCode')    
    for sub in other:
        if sub.tag == 'ExpirationDate':
            continue
            
        s = et.SubElement(e, sub.tag)
        s.text = sub.text
        s.tail = '\n\t\t'
        if sub.tag == 'Basis':
            s.set('public-id', sub.attrib['public-id'])        
        
    return e
        
def newElement(parent, tag, txt=None):
    e = et.SubElement(parent, tag)
    if txt is not None:
        e.text = text
        
    return e
    
def newPublicId():
    global PUBLIC_ID_COUNTER
    PUBLIC_ID_COUNTER += 1
    return f'{PUBLIC_ID_PREFIX}:{str(PUBLIC_ID_COUNTER)}'

def updateHazardGroup(el, t):
    #print('updateHazardGroup', t)
    ne = copyElement(el)
    ne.set('public-id', newPublicId())
    ne.find('EffectiveDate').text = NEW_EFFECTIVE_DATE    
    ne.find('HazardGrade_Ext').text = str(t[2])
    root.append(ne)
    
    phrase = ne.find('Classification')
    pid = ne.get('public-id')
    log.append(f'"{t[0]}",{t[1]},update,{t[2]},{pid},{phrase.text}')
    return ne

def convertUpdates():    
    for jur in states:
        change_map[jur] = {}        
        stateUpdates = change_map[jur]
        for codeGrade in changed_codes[jur]:
            stateUpdates[codeGrade[0]] = codeGrade[1]
    '''
    print(len(change_map))
    for j in states:
        print(len(change_map[j]))    
    ''' 

def clean(fname):
    if os.path.exists(fname):
        os.remove(fname)
        print(f'cleaned {fname}')
    
def flattenClasscodeFile(srcFile, destFile):
    tree = et.parse(srcFile)
    root = tree.getroot()
    print('root', root)
    
    allCodes = root.findall('WC7ClassCode')
    print('Total codes =', len(allCodes), '\n', '-'*99)
    
    with open(destFile, 'w') as fout:  
        # header
        fout.write('code,state,hazardGroup,entHazardGrade,effective,expires,desc,stdEx,construction,risk,type,disease\n')
        # row-per-classcode
        for classcode in allCodes:
            code = safe(classcode.find('Code'))
            jur = safe(classcode.find('Jurisdiction'))
            hg = safe(classcode.find('HazardGrade_Ext'))        
            desc = safe(classcode.find('Classification'))
            eff = safe(classcode.find('EffectiveDate'))
            ehg = safe(classcode.find('EnterpriseHazardGrade_Ext'))
            exp = safe(classcode.find('ExpirationDate'))
            stdex = safe(classcode.find('StdException_Ext'))
            ct = safe(classcode.find('ConstructionType'))
            rl = safe(classcode.find('RiskLevel_Ext'))
            cct = safe(classcode.find('ClassCodeType'))
            dt = safe(classcode.find('DiseaseType'))

            fout.write(f'"{code}","{jur}","{hg}","{ehg}","{eff}","{exp}","{desc}","{stdex}","{ct}","{rl}","{cct}","{dt}"\n')
                       


# In[29]:


clean('wc7_class_codes_updated.xml')
clean('wc7_class_codes_updated.xml.pretty.xml')
clean('log.csv')
    
convertUpdates()
print('states in change map', change_map.keys())
    
print('previous output cleaned')
tree = et.parse('wc7_class_codes.xml')
root = tree.getroot()
print(root)


# In[30]:


allCodes = root.findall('WC7ClassCode')
print('Total codes =', len(allCodes), '\n', '-'*99)

def safe(o):    
    if o is None:
        return '-'
    else:
        return o.text

updates = 0
for classcode in allCodes:
    code = safe(classcode.find('Code'))
    jur = safe(classcode.find('Jurisdiction'))
    hg = safe(classcode.find('HazardGrade_Ext'))        
    
    if jur == '-' or jur not in change_map:
        continue    
    
    tup = (code, jur, hg)
    if code in change_map[jur]:
        newHG = change_map[jur][code]
        expireHazardGroup(classcode, tup)
        
        tup = (code, jur, newHG)
        updateHazardGroup(classcode, tup)        
        
        updates += 1
        

tree.write('wc7_class_codes_updated.xml', method='xml')
print('Output generated, total updates = ', updates)


# Following doesn't work (leaving for posterity):
# 
# `
# from lxml import etree as lx
# parser = lx.XMLParser(remove_blank_text=True)
# lx_tree = lx.parse("wc7_class_codes_updated.xml", parser)
# lx_tree.write("wc7_class_codes_pretty.xml", pretty_print=True, encoding='iso-8859-1')
# print('Output formatted')
# `

# In[31]:


with open('log.csv', mode='w') as logFile:       
    for line in sorted(log):
        logFile.write(f'{line} \n')
        
print('Log file written')


# In[32]:


from bs4 import BeautifulSoup
import re


# In[33]:


def formatXml(filepath):
    with open(filepath, encoding='utf8') as fin:
        xml = BeautifulSoup(fin, "xml").prettify()

    text_re = re.compile('>\n\s+([^<>\s].*?)\n\s+</', re.DOTALL)    
    betterXml = text_re.sub('>\g<1></', xml)
        
    fnameout = f'{filepath}.pretty.xml'
    with open(fnameout, "w", encoding='utf8') as fout:
        fout.write(str(betterXml))
        
    print(f'Output written to {fnameout}')


# In[34]:


formatXml('wc7_class_codes_updated.xml')


# In[35]:


clean('validation_original_file.csv')
clean('validation_resulting_file.csv')
flattenClasscodeFile('wc7_class_codes.xml','validation_original_file.csv')
flattenClasscodeFile('wc7_class_codes_updated.xml.pretty.xml','validation_resulting_file.csv')

