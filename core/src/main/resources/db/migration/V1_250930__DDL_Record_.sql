CREATE TABLE record
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    create_time   datetime              NOT NULL,
    update_time   datetime              NOT NULL,
    maker         VARCHAR(255)          NULL,
    name          VARCHAR(255)          NOT NULL,
    type          SMALLINT              NOT NULL,
    status        SMALLINT              NOT NULL,
    `description` VARCHAR(255)          NULL,
    remark        VARCHAR(255)          NULL,
    CONSTRAINT pk_record PRIMARY KEY (id)
);
