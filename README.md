# gtr-scraper

## Requirements

Neo4j server 3.4+ is required because this adds support for temporal data types.

## Import data

First, run the migrations: `lein migrate`

You need to import the data in dependency order.

First import the persons, then import the projects, then import the funds

Also you need to run the database migrations with lein migrate.

* (insert-all-persons!)
* (insert-all-projects!)
* (insert-all-funds!)

## Mirroring the PostgreSQL graph to Neo4j

    python3 scripts/export_to_neo4j.py

## Querying graph

    MATCH (pr:Project)<-[:PARTICIPATED_IN]-(pe:Person)
    WHERE pr.created_date > date("2017-01-01")
    RETURN pr, pe;


## Authentication

Run this command to see what files need the real authentication details.

    grep -lr xyzzy .

The real details are found in the `gtr.pp` Puppet manifest.
