CREATE TABLE IF NOT EXISTS `ingredients`
(
    `id`          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL UNIQUE,
    `category_id` INT UNSIGNED,
    FOREIGN KEY (`category_id`) REFERENCES `ingredient_categories` (`id`) ON DELETE SET NULL
);
