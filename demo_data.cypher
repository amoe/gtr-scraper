CREATE (pe:Person {id: 1, name: "Dave", gender: "male"});
CREATE (pe:Person {id: 2, name: "Monika", gender: "female"});

CREATE (pr:Project {id: 3, name: "Demo Project"});

CREATE (f:Fund {id: 4, name: "Test Fund"});

MATCH (pe:Person {id: 1}), (pr:Project {id: 3}), (f:Fund {id: 4})
CREATE (pe)-[:PARTICIPATED_IN]->(pr),
       (pr)-[:FUNDED_BY]->(f);

// CREATE
//   (s1:Sentence {tokens: ["Jake", "wanted", "the", "dog", "the", "neighbours", "had"]}),
//   (t1:Token {content: "Jake"}),
//   (t2:Token {content: "wanted"}),
//   (det:Token {content: "the"}),
//   (t3:Token {content: "dog"}),
//   (t4:Token {content: "neighbours"}),
//   (t5:Token {content: "had"}),
//   (s1)-[:CONTAINS {index: 0, firstToken: true}]->(t1),
//   (s1)-[:CONTAINS {index: 1}]->(t2),
//   (s1)-[:CONTAINS {index: 2}]->(det),
//   (s1)-[:CONTAINS {index: 3}]->(t3),
//   (s1)-[:CONTAINS {index: 4}]->(det),
//   (s1)-[:CONTAINS {index: 5}]->(t4),
//   (s1)-[:CONTAINS {index: 6, lastToken: true}]->(t5);


// CREATE (s1:Sentence {tokens: ["Oyl", "shop"]}),
//        (s2:Sentence {tokens: ["oil", "broker"]}),
//        (n1:Token {content: "Oyl"}),
//        (n2:Token {content: "shop"}),
//        (n3:Token {content: "oil"}),
//        (n4:Token {content: "broker"}),
//        (s1)-[:CONTAINS {index: 0}]->(n1),
//        (s1)-[:CONTAINS {index: 1}]->(n2),
//        (s2)-[:CONTAINS {index: 0}]->(n3),
//        (s2)-[:CONTAINS {index: 1}]->(n4);
    

         
// CREATE (s1:Sentence {tokens: ["the", "quick", "brown", "fox"]}),
//        (s2:Sentence {tokens: ["the", "quick", "brown", "bear"]}),
//        (n1:Token {content: "the"}),
//        (n2:Token {content: "quick"}),
//        (n3:Token {content: "brown"}),
//        (n4:Token {content: "fox"}),
//        (n5:Token {content: "bear"}),
//        (s1)-[:CONTAINS {index: 0}]->(n1),
//        (s1)-[:CONTAINS {index: 1}]->(n2),
//        (s1)-[:CONTAINS {index: 2}]->(n3),
//        (s1)-[:CONTAINS {index: 3}]->(n4),
//        (s2)-[:CONTAINS {index: 0}]->(n1),
//        (s2)-[:CONTAINS {index: 1}]->(n2),
//        (s2)-[:CONTAINS {index: 2}]->(n3),
//        (s2)-[:CONTAINS {index: 3}]->(n5);

// CREATE (s1:Sentence {tokens: ["lonely", "island"]}),
//        (n1:Token {content: "lonely"}),
//        (n2:Token {content: "island"}),
//        (s1)-[:CONTAINS {index: 0}]->(n1),
//        (s1)-[:CONTAINS {index: 1}]->(n2);


// // MATCH (s1:Sentence)-[r:CONTAINS]->(t1:Token)
// // RETURN t1, r.index
// // ORDER BY r.index;

