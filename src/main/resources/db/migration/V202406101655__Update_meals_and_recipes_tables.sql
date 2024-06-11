-- V202406101655__Update_meals_and_recipes_tables.sql


-- Add cookbook and page columns to the recipes table
ALTER TABLE recipes
    ADD COLUMN cookbook VARCHAR(255) NULL,
    ADD COLUMN page INT NULL;

-- Create the join table for the many-to-many relationship between meals and recipes
CREATE TABLE meal_recipes (
  meal_id INT UNSIGNED NOT NULL,
  recipe_id INT UNSIGNED NOT NULL,
  PRIMARY KEY (meal_id, recipe_id),
  FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
  FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
