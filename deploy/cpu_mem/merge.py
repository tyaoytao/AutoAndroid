#!/usr/local/python
# coding=UTF-8
import os
import sys
def writecode(file_html,file_log):
	fp = file(file_html)
	lines = []
	for line in fp: # 内置的迭代器, 效率很高
	    lines.append(line)
	fp.close()
	fileadd = open(file_log,"r") 
	contentadd = fileadd.read()
	lines.insert(9, contentadd)
	s = ''.join(lines)
	fp = file(file_html, 'w')
	fp.write(s)
	fp.close()
	
writecode(sys.argv[1], sys.argv[2])