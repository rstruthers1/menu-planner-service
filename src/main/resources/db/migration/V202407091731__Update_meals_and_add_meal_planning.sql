-- Add group_id and suggested_by_user_id to meals table
ALTER TABLE meals
    ADD COLUMN group_id INT UNSIGNED,
    ADD COLUMN suggested_by_user_id INT UNSIGNED,
    ADD CONSTRAINT fk_meals_group
        FOREIGN KEY (group_id) REFERENCES user_groups(id),
    ADD CONSTRAINT fk_suggested_by_user
        FOREIGN KEY (suggested_by_user_id) REFERENCES users(id);

-- Create meal_planning_days table
CREATE TABLE IF NOT EXISTS meal_planning_days (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    group_id INT UNSIGNED NOT NULL,
    date DATE NOT NULL,
    weather_high_temp DECIMAL(5,2),
    weather_low_temp DECIMAL(5,2),
    weather_description VARCHAR(255),
    temperature_unit VARCHAR(1) CHECK (temperature_unit IN ('C', 'F')),
    FOREIGN KEY (group_id) REFERENCES user_groups(id)
);

-- Create meal_planning_day_meals table
CREATE TABLE IF NOT EXISTS meal_planning_day_meals (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    meal_planning_day_id INT UNSIGNED NOT NULL,
    meal_id INT UNSIGNED NOT NULL,
    meal_type VARCHAR(255) NOT NULL,
    FOREIGN KEY (meal_planning_day_id) REFERENCES meal_planning_days(id),
    FOREIGN KEY (meal_id) REFERENCES meals(id)
);

-- Create foods table
CREATE TABLE IF NOT EXISTS foods (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create meal_foods table
CREATE TABLE IF NOT EXISTS meal_foods (
    meal_id INT UNSIGNED NOT NULL,
    food_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (meal_id) REFERENCES meals(id),
    FOREIGN KEY (food_id) REFERENCES foods(id),
    PRIMARY KEY (meal_id, food_id)
);

-- Create meal_cooks table
CREATE TABLE IF NOT EXISTS meal_cooks (
    meal_planning_day_meal_id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (meal_planning_day_meal_id) REFERENCES meal_planning_day_meals(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY (meal_planning_day_meal_id, user_id)
);
