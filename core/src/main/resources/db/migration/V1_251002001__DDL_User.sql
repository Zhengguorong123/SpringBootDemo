CREATE TABLE user
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    account     VARCHAR(255)          NULL,
    name        VARCHAR(255)          NULL,
    email       VARCHAR(255)          NULL,
    create_time datetime              NULL,
    update_time datetime              NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
