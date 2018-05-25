#! /usr/bin/python3 


import psycopg2
import subprocess

conn = psycopg2.connect(host='localhost', dbname='gtr', user='gtr', password='jz3KTupRvDzf9Io1')

cur = conn.cursor()

cur.execute("SELECT DISTINCT first_name FROM person;")

row = cur.fetchone()
while row:
   name = row[0]
   result = subprocess.check_output(['ruby', '/home/amoe/dev/gtr-scraper/gender_detector_wrapper.rb', name]).rstrip().decode('utf-8')
   print("%s\t%s" % (name, result))
   row = cur.fetchone()

cur.close()
conn.close()
