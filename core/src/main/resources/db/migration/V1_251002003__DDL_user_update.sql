ALTER TABLE user
    ADD password_hash VARCHAR(255) NULL;

ALTER TABLE user
    ADD salt VARCHAR(255) NULL;

ALTER TABLE user
    rename column name to username;

