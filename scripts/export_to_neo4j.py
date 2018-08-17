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

QUERY_PROJECTS = """
    SELECT p.id, p.created_date, p.title
    FROM project p;
"""

CREATE_PERSON_NODES = """
    CREATE (p:Person {metadata})
"""

CREATE_PROJECT_NODES = """
    CREATE (p:Project {metadata})
"""

QUERY_PERSON_PROJECT_LINKS = """
    SELECT pp.person_id, pp.project_id, pp.role_code
    FROM person_project pp;
"""

LINK_PERSONS_WITH_PROJECTS = """
    MATCH (p1:Person {id: {person_id}}), (p2:Project {id: {project_id}})
    CREATE (p1)-[r:PARTICIPATED_IN {role_code: {role_code}}]->(p2);
"""

QUERY_FUNDS = """
    SELECT f.id, f.funded_id
    FROM fund f;
"""

CREATE_FUNDS = """
    CREATE (f:Fund {id: {fund_id}}),
           (p:Project {id: {project_id}})-[r:FUNDED_BY]->(f);
"""

class Neo4jExporter(object):
    def load_person_project_links(self):
        cur.execute(QUERY_PERSON_PROJECT_LINKS)
        i = 1
        for x in cur:
            print("adding link", i, dict(x))
            i += 1
            with driver.session() as session:
                with session.begin_transaction() as tx:
                    tx.run(LINK_PERSONS_WITH_PROJECTS, dict(x))

    def run(self, args):
        print("hello")
        print(pg_conn)
        print(driver)

        cur.execute(QUERY_PERSONS)
        with driver.session() as session:
            with session.begin_transaction() as tx:
                for x in cur:
                    tx.run(CREATE_PERSON_NODES, {'metadata': dict(x)})

        cur.execute(QUERY_PROJECTS)
        with driver.session() as session:
            with session.begin_transaction() as tx:
                for x in cur:
                    tx.run(CREATE_PROJECT_NODES, {'metadata': dict(x)})


        cur.execute(QUERY_FUNDS)
        with driver.session() as session:
            with session.begin_transaction() as tx:
                for x in cur:
                    tx.run(CREATE_FUNDS, {'fund_id': x['id'],
                                          'project_id': x['funded_id']})
        
        self.load_person_project_links()

        print("closing connection")
        driver.close()
        print("connection closed")
        cur.close()
        pg_conn.close()
            
        

if __name__ == '__main__':
    obj = Neo4jExporter()
    obj.run(sys.argv[1:])
