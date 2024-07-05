ALTER TABLE recipes
    ADD COLUMN user_id INT UNSIGNED NULL,
    ADD COLUMN group_id INT UNSIGNED NULL,
    ADD CONSTRAINT fk_user_recipe
        FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_group_recipe
        FOREIGN KEY (group_id) REFERENCES user_groups (id);
