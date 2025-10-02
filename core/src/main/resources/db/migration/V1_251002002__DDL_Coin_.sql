CREATE TABLE coin
(
    id          BIGINT   NOT NULL,
    number      BIGINT   NULL,
    update_time datetime NULL,
    CONSTRAINT pk_coin PRIMARY KEY (id)
);

ALTER TABLE coin
    ADD CONSTRAINT FK_COIN_ON_ID FOREIGN KEY (id) REFERENCES user (id);
