CREATE TABLE if not exists roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20)
);
INSERT IGNORE INTO roles (id, name) VALUES (1,'ROLE_USER');