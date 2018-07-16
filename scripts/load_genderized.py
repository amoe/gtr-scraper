import ast
import json
import sys

with open('scratch.dat', 'r') as f:
    data = ast.literal_eval(f.read())

print(len(data))
