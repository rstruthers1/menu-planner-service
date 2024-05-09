ALTER TABLE `recipes`
    DROP INDEX `name`,  -- Drops the unique index on the name column
    MODIFY `name` VARCHAR(255) NOT NULL;  -- Ensures the column is still not nullable
