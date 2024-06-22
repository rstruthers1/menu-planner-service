-- V202406211309__Create_cookbooks_table.sql

-- Create the cookbooks table
CREATE TABLE cookbooks (
   id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   image_file_name VARCHAR(255) NOT NULL
);

-- Update the recipes table to include a foreign key to the cookbooks table
ALTER TABLE recipes
    ADD COLUMN cookbook_id INT UNSIGNED,
    ADD CONSTRAINT fk_cookbook
        FOREIGN KEY (cookbook_id) REFERENCES cookbooks(id);


