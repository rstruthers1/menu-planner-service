ALTER TABLE meal_planning_days
ADD CONSTRAINT meal_planning_days_group_date_unique UNIQUE (group_id, date);

