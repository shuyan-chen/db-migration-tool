INSERT INTO User (name, email) VALUES
                                   ('Alice', 'alice@example.com'),
                                   ('Bob', 'bob@example.com'),
                                   ('Charlie', 'charlie@example.com'),
                                   ('David', 'david@example.com'),
                                   ('Eve', 'eve@example.com'),
                                   ('Frank', 'frank@example.com'),
                                   ('Grace', 'grace@example.com'),
                                   ('Hank', 'hank@example.com'),
                                   ('Ivy', 'ivy@example.com'),
                                   ('Jack', 'jack@example.com');


INSERT INTO Profile (user_id, bio) VALUES
                                       (1, 'Bio of Alice'),
                                       (2, 'Bio of Bob'),
                                       (3, 'Bio of Charlie'),
                                       (4, 'Bio of David'),
                                       (5, 'Bio of Eve'),
                                       (6, 'Bio of Frank'),
                                       (7, 'Bio of Grace'),
                                       (8, 'Bio of Hank'),
                                       (9, 'Bio of Ivy'),
                                       (10, 'Bio of Jack');


INSERT INTO Post (user_id, title, content) VALUES
                                               (1, 'Alice Post 1', 'Content of Alice Post 1'),
                                               (1, 'Alice Post 2', 'Content of Alice Post 2'),
                                               (2, 'Bob Post 1', 'Content of Bob Post 1'),
                                               (2, 'Bob Post 2', 'Content of Bob Post 2'),
                                               (3, 'Charlie Post 1', 'Content of Charlie Post 1'),
                                               (3, 'Charlie Post 2', 'Content of Charlie Post 2'),
                                               (4, 'David Post 1', 'Content of David Post 1'),
                                               (4, 'David Post 2', 'Content of David Post 2'),
                                               (5, 'Eve Post 1', 'Content of Eve Post 1'),
                                               (5, 'Eve Post 2', 'Content of Eve Post 2'),
                                               (6, 'Frank Post 1', 'Content of Frank Post 1'),
                                               (6, 'Frank Post 2', 'Content of Frank Post 2'),
                                               (7, 'Grace Post 1', 'Content of Grace Post 1'),
                                               (7, 'Grace Post 2', 'Content of Grace Post 2'),
                                               (8, 'Hank Post 1', 'Content of Hank Post 1'),
                                               (8, 'Hank Post 2', 'Content of Hank Post 2'),
                                               (9, 'Ivy Post 1', 'Content of Ivy Post 1'),
                                               (9, 'Ivy Post 2', 'Content of Ivy Post 2'),
                                               (10, 'Jack Post 1', 'Content of Jack Post 1'),
                                               (10, 'Jack Post 2', 'Content of Jack Post 2');


INSERT INTO Tag (name) VALUES
                           ('Tag1'),
                           ('Tag2'),
                           ('Tag3'),
                           ('Tag4'),
                           ('Tag5'),
                           ('Tag6'),
                           ('Tag7'),
                           ('Tag8'),
                           ('Tag9'),
                           ('Tag10');


INSERT INTO Post_Tag (post_id, tag_id) VALUES
                                           (1, 1),
                                           (1, 2),
                                           (2, 2),
                                           (2, 3),
                                           (3, 3),
                                           (3, 4),
                                           (4, 4),
                                           (4, 5),
                                           (5, 5),
                                           (5, 6),
                                           (6, 6),
                                           (6, 7),
                                           (7, 7),
                                           (7, 8),
                                           (8, 8),
                                           (8, 9),
                                           (9, 9),
                                           (9, 10),
                                           (10, 10),
                                           (10, 1),
                                           (1, 3),
                                           (2, 4),
                                           (3, 5),
                                           (4, 6),
                                           (5, 7),
                                           (6, 8),
                                           (7, 9),
                                           (8, 10),
                                           (9, 1),
                                           (10, 2);