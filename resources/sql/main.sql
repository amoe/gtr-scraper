-- main.sql

-- :name insert-person!
-- :command :insert
-- :doc Insert a person
INSERT INTO person (id, first_name, last_name)
VALUES (:id, :first_name, :last_name);

-- :name insert-project!
-- :command :insert
-- :doc Insert a project
INSERT INTO project (id)
VALUES (:id);

-- :name link-person-to-project!
-- :command :insert
-- :doc Link a person to a project
INSERT INTO person_project (person_id, project_id)
VALUES (:person_id, :project_id);

-- :name query-if-person-exists
-- :command :query
-- :doc Check if the person exists
SELECT EXISTS(SELECT id FROM person WHERE id = :id) AS person_exists_p;
