#! /usr/bin/python3 

import psycopg2
import subprocess

API_KEY = "274e3c4e55e2c9aef8bdf0c659e5cbc9"

conn = psycopg2.connect(host='localhost', dbname='gtr', user='gtr', password='jz3KTupRvDzf9Io1')

cur = conn.cursor()

from genderize import Genderize

genderize = Genderize(
    user_agent='GenderizeDocs/0.0',
    api_key=API_KEY
)


cur.execute("SELECT DISTINCT first_name FROM person;")

result = cur.fetchall()
names = [r[0] for r in result]
print(names)
result2 = genderize.get(names)
print(result2)

# row = cur.fetchone()
# while row:
#    name = row[0]
#    result = genderize.get([name])
#    print(result)
#    row = cur.fetchone()

cur.close()
conn.close()
