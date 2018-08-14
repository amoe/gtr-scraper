-- Static domain table of roles

CREATE TABLE project_role (
    code VARCHAR(16) PRIMARY KEY,
    description VARCHAR(80) NOT NULL
);

INSERT INTO project_role (code, description) VALUES ('COI_PER', '');
INSERT INTO project_role (code, description) VALUES ('FELLOW_PER', '');
INSERT INTO project_role (code, description) VALUES ('PI_PER', '');
INSERT INTO project_role (code, description) VALUES ('PM_PER', '');
INSERT INTO project_role (code, description) VALUES ('RESEARCH_COI_PER', '');
INSERT INTO project_role (code, description) VALUES ('RESEARCH_PER', '');
INSERT INTO project_role (code, description) VALUES ('STUDENT_PER', '');
INSERT INTO project_role (code, description) VALUES ('SUPER_PER', '');
INSERT INTO project_role (code, description) VALUES ('TGH_PER', '');

CREATE TABLE person (
    id UUID PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    gender VARCHAR(8) NULL
);

CREATE TABLE project (
    id UUID PRIMARY KEY    
);

CREATE TABLE person_project (
    person_id UUID REFERENCES person (id) NOT NULL,
    project_id UUID REFERENCES project (id) NOT NULL,
    role_code VARCHAR(16) REFERENCES project_role (code) NOT NULL
);

CREATE TABLE fund (
    id UUID PRIMARY KEY,
    funded_id UUID REFERENCES project (id) NOT NULL,
    value_pounds NUMERIC(19, 4) NOT NULL
);


