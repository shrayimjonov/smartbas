-- Global sequence for BaseEntity IDs
CREATE SEQUENCE usermanual_seq
    START WITH 1
    INCREMENT BY 50
    NOCACHE
    NOCYCLE;

-- Table: user_manual_section
CREATE TABLE user_manual_section (
    id                  NUMBER(19)         PRIMARY KEY,
    name                VARCHAR2(255 CHAR)  NOT NULL,
    description         VARCHAR2(1000 CHAR),
    parent_id           NUMBER(19),
    platform_type       VARCHAR2(30 CHAR)   NOT NULL,
    created_date        TIMESTAMP(6)        NOT NULL,
    last_modified_date  TIMESTAMP(6)        NOT NULL
);

-- Unique constraint on section name
ALTER TABLE user_manual_section
    ADD CONSTRAINT uk_user_manual_section_name UNIQUE (name);

-- Self-referencing foreign key for parent/child sections
ALTER TABLE user_manual_section
    ADD CONSTRAINT fk_ums_parent
        FOREIGN KEY (parent_id)
        REFERENCES user_manual_section (id);

-- Table: user_manual_version
CREATE TABLE user_manual_version (
    id                  NUMBER(19)         PRIMARY KEY,
    version_number      VARCHAR2(20 CHAR)  NOT NULL,
    section_id          NUMBER(19),
    status              VARCHAR2(30 CHAR)  NOT NULL,
    created_date        TIMESTAMP(6)       NOT NULL,
    last_modified_date  TIMESTAMP(6)       NOT NULL
);

-- Unique version per section
ALTER TABLE user_manual_version
    ADD CONSTRAINT uk_umv_version_manual UNIQUE (version_number, section_id);

-- FK to section
ALTER TABLE user_manual_version
    ADD CONSTRAINT fk_umv_section
        FOREIGN KEY (section_id)
        REFERENCES user_manual_section (id);
