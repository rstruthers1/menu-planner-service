CREATE TABLE user_groups
(
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_group_members
(
    user_id INT UNSIGNED NOT NULL,
    group_id INT UNSIGNED NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_group
        FOREIGN KEY (group_id) REFERENCES user_groups (id),
    PRIMARY KEY (user_id, group_id)
);

