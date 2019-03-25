CREATE (pe:Person {id: 1, name: "Dave", gender: "male"});
CREATE (pe:Person {id: 2, name: "Monika", gender: "female"});

CREATE (pr:Project {id: 3, name: "Demo Project", created_date: date("2018-01-01")});

CREATE (f:Fund {id: 4, name: "Test Fund"});

MATCH (pe:Person {id: 1}), (pr:Project {id: 3}), (f:Fund {id: 4})
CREATE (pe)-[:PARTICIPATED_IN]->(pr),
       (pr)-[:FUNDED_BY]->(f);


CREATE (pr:Project {id: 5, name: "A much older project", created_date: date("2015-01-01")});

CREATE (f:Fund {id: 6, name: "Funding for a much older project"});

MATCH (pe:Person), (pr:Project {id: 5}), (f:Fund {id: 6})
CREATE (pe)-[:PARTICIPATED_IN]->(pr),
       (pr)-[:FUNDED_BY]->(f);

