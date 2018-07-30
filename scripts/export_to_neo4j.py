#! /usr/bin/python3

import sys
import psycopg2
import psycopg2.extras
import neo4j.v1

NEO4J_HOSTNAME = 'localhost'
NEO4J_PORT = 7689
NEO4J_USERNAME = 'neo4j'
NEO4J_PASSWORD = 'password'

POSTGRESQL_HOSTNAME = 'localhost'
POSTGRESQL_DATABASE = 'gtr'
POSTGRESQL_USER = 'gtr'
POSTGRESQL_PASSWORD = 'jz3KTupRvDzf9Io1'

pg_conn = psycopg2.connect(
    host=POSTGRESQL_HOSTNAME, dbname=POSTGRESQL_DATABASE, user=POSTGRESQL_USER, password=POSTGRESQL_PASSWORD,
    cursor_factory=psycopg2.extras.DictCursor
)
cur = pg_conn.cursor()

credentials = (NEO4J_USERNAME, NEO4J_PASSWORD)
uri = "bolt://%s:%d" % (NEO4J_HOSTNAME, NEO4J_PORT)
driver = neo4j.v1.GraphDatabase.driver(uri, auth=credentials)


QUERY_PERSONS = """
    SELECT p.id, p.first_name, p.last_name, p.gender
    FROM person p;
"""

CREATE_PERSON_NODES = """
    CREATE (p:Person {metadata})
"""


class Neo4jExporter(object):
    def run(self, args):
        print("hello")
        print(pg_conn)
        print(driver)


        cur.execute(QUERY_PERSONS)

        with driver.session() as session:
            with session.begin_transaction() as tx:
                for x in cur:
                    tx.run(CREATE_PERSON_NODES, {'metadata': dict(x)})

        driver.close()
        cur.close()
        conn.close()
            
        

if __name__ == '__main__':
    obj = Neo4jExporter()
    obj.run(sys.argv[1:])
