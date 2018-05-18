CREATE TABLE person (
    id UUID PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL
);

CREATE TABLE project (
    id UUID PRIMARY KEY    
);

CREATE TABLE person_project (
    person_id UUID REFERENCES person (id) NOT NULL,
    project_id UUID REFERENCES project (id) NOT NULL
);

CREATE TABLE fund (
    id UUID PRIMARY KEY,
    funded_id UUID REFERENCES project (id) NOT NULL
);

