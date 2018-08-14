# gtr-scraper

## Import data

First, run the migrations.  `lein run migrate`

You need to import the data in dependency order.

First import the persons, then import the projects, then import the funds

Also you need to run the database migrations with lein migrate.

* (insert-all-persons!)
* (insert-all-projects!)
* (insert-all-funds!)
