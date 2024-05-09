ALTER TABLE `ingredients`
    ADD `plural_form` VARCHAR(255) DEFAULT NULL,
    ADD CONSTRAINT `UK_ingredient_name` UNIQUE (`name`);
