import_data:
	lein run -m gtr-scraper.data-service/do-full-import!

reset_database:
	-sudo sudo -u postgres dropdb gtr
	-sudo puppet agent --test
	lein migrate

reset_neo4j:
	echo "MATCH (n) DETACH DELETE n;" | cypher-shell
	cypher-shell < demo_data.cypher
