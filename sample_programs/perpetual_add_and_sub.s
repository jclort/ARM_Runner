;@author Mark
subtraction:
SUB x0,x0,10
CMP x0,0
BGT subtraction
addition:
ADD x0,x0,3
CMP x0,0
BLT addition
B subtraction