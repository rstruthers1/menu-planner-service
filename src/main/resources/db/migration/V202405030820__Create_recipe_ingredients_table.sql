CREATE TABLE IF NOT EXISTS `recipe_ingredients`
(
    `recipe_id`     INT UNSIGNED,
    `ingredient_id` INT UNSIGNED,
    `amount`        FLOAT,
    `unit_id`       INT UNSIGNED,
    `preparation`   VARCHAR(255),
    `notes`         TEXT,
    PRIMARY KEY (`recipe_id`, `ingredient_id`),
    FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`) ON DELETE SET NULL
);
