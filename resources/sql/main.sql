-- main.sql

-- :name insert-person!
-- :command :insert
-- :doc Insert a person
INSERT INTO person (id, first_name, last_name, gender, created_date)
VALUES (:id, :first_name, :last_name, :gender, :created_date);

-- :name insert-project!
-- :command :insert
-- :doc Insert a project
INSERT INTO project (id, created_date, title, abstract)
VALUES (:id, :created_date, :title, :abstract);

-- :name link-person-to-project!
-- :command :insert
-- :doc Link a person to a project
INSERT INTO person_project (person_id, project_id, role_code)
VALUES (:person_id, :project_id, :role_code);

-- :name insert-fund!
-- :command :insert
-- :doc Insert a fund
INSERT INTO fund (id, funded_id, value_pounds, start_date, end_date)
VALUES (:id, :funded_id, :value_pounds, :start_date, :end_date);

-- :name query-if-person-exists
-- :command :query
-- :doc Check if the person exists
SELECT EXISTS(SELECT id FROM person WHERE id = :id) AS person_exists_p;

-- :name query-if-project-exists
-- :command :query
-- :doc Check if the project exists
SELECT EXISTS(SELECT id FROM project WHERE id = :id) AS project_exists_p;

-- :name update-gender-for-name!
-- :command :execute
-- :doc Update gender for a given name
UPDATE person SET gender = :gender WHERE first_name = :first_name;
