CREATE TABLE IF NOT EXISTS `brand_ingredients`
(
    `brand_id`      INT UNSIGNED NOT NULL,
    `ingredient_id` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`brand_id`, `ingredient_id`),
    FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE CASCADE
);
