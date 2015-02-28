#!/usr/bin/env python3


import sys
import re
import operator
from collections import defaultdict

courses = []
majorcourses = []
coursesbydept = defaultdict(list)
gpa = {"A":4.0,"A-":3.7,"B+":3.3,"B":3.0,"B-":2.7,"C+":2.3,"C":2.0,"C-":1.7,"D+":1.3,"D":1.0,"F":0.0}

if(sys.stdin.isatty()):
	print("Input your courses in the following format, and input a blank line to calculate:")
	print("[Class Code] [in-major/not in-major] [Grade]")
	print("ex: \"CMSC101 y A\" or \"HUMA101 n C+\"")

# process input

for lineno,line in enumerate(sys.stdin,start=1):
	if(sys.stdin.isatty() and line == "\n"):
		break
	linesplt = line.split()
	dept = linesplt[0][0:4]
	major = (linesplt[1].lower()=='y')
	grade = gpa[linesplt[2].upper()]
	coursesbydept[dept].append(grade)
	courses.append(grade)
	if major:
		majorcourses.append(grade)
		
	
	
print("Overall GPA: {}".format(sum(courses)/len(courses)))
print("Major GPA: {}".format(sum(majorcourses)/len(majorcourses)))

print()
print("Departamental GPA(s):")
for dept in coursesbydept:
	print("{}: {}".format(dept,sum(coursesbydept[dept])/len(coursesbydept[dept])))
