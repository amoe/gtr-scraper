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

These processes are encapsulated in the Makefile tasks.

## Specifying the database password

Specify the database password in your local `profiles.clj` file (within the root
of this repository).  It should look something like this:

    {:dev {:env {:database-password "xyzzy"}}}

## Exporting the database

    pg_dump -F c -Z 9 gtr > gtr.backup

This will create a compressed dump.  To restore that dump, you can use the
following:

    pg_restore -d gtr gtr.backup

## Mirroring the PostgreSQL database to a Neo4j graph

    python3 scripts/export_to_neo4j.py

For some reason this is in Python, although it should probably also be in
Clojure; then we wouldn't have to specify the authentication details twice.

## Querying graph

    MATCH (pr:Project)<-[:PARTICIPATED_IN]-(pe:Person)
    WHERE pr.created_date > date("2017-01-01")
    RETURN pr, pe;

## Authentication

Run this command to see what files need the real authentication details.

    grep -lr xyzzy .

The real details are found in the `gtr.pp` Puppet manifest.


Yow! x1
