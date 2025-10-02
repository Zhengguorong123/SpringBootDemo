ALTER TABLE user
    ADD CONSTRAINT uc_user_account UNIQUE (account);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE user
    MODIFY account VARCHAR(50);

ALTER TABLE user
    MODIFY account VARCHAR(50) NOT NULL;

ALTER TABLE user
    MODIFY create_time datetime NOT NULL;

ALTER TABLE user
    MODIFY email VARCHAR(255) NOT NULL;

ALTER TABLE user
    MODIFY update_time datetime NOT NULL;

ALTER TABLE user
    MODIFY username VARCHAR(255) NOT NULL;
