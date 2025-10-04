CREATE TABLE invite_code
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    create_time datetime              NOT NULL,
    update_time datetime              NOT NULL,
    code        VARCHAR(20)           NOT NULL,
    owner_id    BIGINT                NOT NULL,
    invitee_id  BIGINT                NULL,
    used        BIT(1)                NOT NULL,
    expire_at   datetime              NULL,
    used_at     datetime              NULL,
    CONSTRAINT pk_invitecode PRIMARY KEY (id)
);

ALTER TABLE invite_code
    ADD CONSTRAINT uc_invitecode_code UNIQUE (code);

ALTER TABLE invite_code
    ADD CONSTRAINT uc_invitecode_invitee UNIQUE (invitee_id);

ALTER TABLE invite_code
    ADD CONSTRAINT FK_INVITECODE_ON_INVITEE FOREIGN KEY (invitee_id) REFERENCES user (id);

ALTER TABLE invite_code
    ADD CONSTRAINT FK_INVITECODE_ON_OWNER FOREIGN KEY (owner_id) REFERENCES user (id);
