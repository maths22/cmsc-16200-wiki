#!/usr/bin/env python3
import sys
import datetime
from copy import deepcopy
import xml.etree.ElementTree as ET

class schedule(object):
    def __init__(self, defcon=None):
        self.mwf = []
        self.tr = []
        self.course_cnt = 0
        for i in range(24*2):
            if defcon==None:
                self.mwf.append(None)
                self.tr.append(None)
            else:
                self.mwf.append(defcon())
                self.tr.append(defcon())
    
                
    def has_n_courses(self,n):
        return self.course_cnt == n
        
    def all_courses(self):
        return list(filter(None, self.mwf))+ list(filter(None, self.tr))
                
    def time_to_block(self,time):
        time = datetime.datetime.strptime(time,"%I:%M %p")
        tot = time.hour*60+time.minute
        return tot/30
        
    def block_to_time(self,block):
        minutes = int((block*30)%60)
        hours = int((block*30)/60)
        time = datetime.time(hours,minutes)
        return datetime.time.strftime(time,"%I:%M %p")
        
    def scheduleXML(self):
        root = ET.Element("schedule");
        mwf_xml = ET.SubElement(root, "mwf")
        tr_xml = ET.SubElement(root, "tr")
        color = [255, 100, 100]
        name = ""
        for i in range(24*2):
            p_mwf = ET.SubElement(mwf_xml, "period")
            p_mwf.attrib["time"]= self.block_to_time(i)
            p_mwf.attrib["block"] = str(i)
            if not (self.mwf[i] == None):
                p_mwf.text = str(self.mwf[i])
                if not name == p_mwf.text:
                    color[0] = (color[0]+75)%255
                    color[1] = (color[1]+150)%255
                    color[2] = (color[2]+225)%255
                p_mwf.attrib["color"] = "{:02x}{:02x}{:02x}".format(color[0],color[1],color[2])
                name = p_mwf.text
        for i in range(24*2): 
            p_tr = ET.SubElement(tr_xml, "period");
            p_tr.attrib["time"]= self.block_to_time(i)
            p_tr.attrib["block"] = str(i)
            if not (self.tr[i] == None):
                p_tr.text = str(self.tr[i])
                if not name == p_tr.text:
                    color[0] = (color[0]+75)%255
                    color[1] = (color[1]+150)%255
                    color[2] = (color[2]+225)%255
                p_tr.attrib["color"] = "{:02x}{:02x}{:02x}".format(color[0],color[1],color[2])
                name = p_tr.text
            
        return root

t1 = ET.parse("courses.xml")
tree = t1.getroot()

schedules = []

courses = schedule(list)
for course in tree:
    name = course.attrib["name"]
    for section in course:
        block = int(courses.time_to_block(section.attrib["time"]))
        sect = (name,section.attrib["number"],int(section.attrib["units"]))
        if section.attrib["day"] == "MWF":
            courses.mwf[block].append(sect)
        elif section.attrib["day"] == "TR":
            courses.tr[block].append(sect)

def remove_course(prds, name):
    ret = []
    for crs in prds:
        app = []
        for course in crs:
            if not course[0] == name:
                app.append(course)   
        ret.append(app)
    return ret

def makeSchedules(day, day_courses, period, schedule):
    if day == "MWF":
        sch_courses = schedule.mwf
    else:
        sch_courses = schedule.tr
    branched = False
    while (not branched) and (period < 24*2):
        if len(day_courses[period]) > 0:
            makeSchedules(day,day_courses,period+1,deepcopy(schedule))
            for course in day_courses[period]:
                for j in range(0, course[2]):
                    sch_courses[period+j] = course[0] + "/" + str(course[1])
                newsch = deepcopy(schedule)
                newsch.course_cnt += 1
                makeSchedules(day,remove_course(day_courses,course[0]),period+course[2],newsch)
            branched = True
        period += 1
    tr_crs = courses.tr
    for course in schedule.all_courses():
        tr_crs = remove_course(tr_crs, str.split(course,"/")[0])
    
    if period == 24*2:
        if day == "MWF":
            makeSchedules("TR", tr_crs, 0,schedule)
        else:
            schedules.append(schedule)
            

makeSchedules("MWF", courses.mwf, 0,schedule())

root = ET.Element("schedules")
i = 0
for sch in schedules:
    if sch.has_n_courses(len(tree)):
        root.append(sch.scheduleXML())
        i += 1
print(i)

ET.ElementTree(root).write("out.xml")

#print(ET.tostring(root, encoding='utf8', method='xml'))

#print(ET.tostring(courses.scheduleXML(), encoding='utf8', method='xml'))

