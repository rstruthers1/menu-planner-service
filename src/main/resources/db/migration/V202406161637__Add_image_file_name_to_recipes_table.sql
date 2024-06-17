-- V202406161637__Add_image_file_name_to_recipes_table.sql

-- Add new column image_file_name to the recipes table
ALTER TABLE recipes
    ADD COLUMN image_file_name VARCHAR(255)


