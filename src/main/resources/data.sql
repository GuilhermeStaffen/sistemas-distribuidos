CREATE TABLE if not exists roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20)
) engine = ndb;

INSERT IGNORE INTO roles (id, name) VALUES (1,'ROLE_USER');

CREATE TABLE if not exists `access_todo` (
  `todo_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=ndb DEFAULT CHARSET=utf8mb4;

CREATE TABLE if not exists `roles` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(20) DEFAULT NULL
) ENGINE=ndb DEFAULT CHARSET=utf8mb4;

CREATE TABLE if not exists `todos` (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `active` tinyint(1) DEFAULT 1,
  `description` varchar(500) NOT NULL,
  `title` varchar(100) NOT NULL
) ENGINE=ndb DEFAULT CHARSET=utf8mb4;

CREATE TABLE if not exists `users` (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL
) ENGINE=ndb DEFAULT CHARSET=utf8mb4;

CREATE TABLE if not exists `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=ndb DEFAULT CHARSET=utf8mb4;
