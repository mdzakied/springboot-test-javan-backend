CREATE TABLE m_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

INSERT INTO m_users (username, password) VALUES ('user', '$2y$10$Xuy0WKZvCYBgwULmRlqSKe1ztPyDY/gdVzqgKmWHtrj9ZaaxynUXm');
