CREATE TABLE IF NOT EXISTS `recipes`
(
    `id`           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name`         VARCHAR(255) NOT NULL UNIQUE,
    `description`  TEXT,
    `instructions` TEXT
);
