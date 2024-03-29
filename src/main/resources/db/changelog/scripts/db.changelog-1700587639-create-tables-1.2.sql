CREATE TABLE IF NOT EXISTS PUBLIC.ORG_GROUP (
    ID UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    DATE_CREATED TIMESTAMP WITHOUT TIME ZONE,
    CREATED_BY CHARACTER VARYING COLLATE pg_catalog."default",
    DATE_UPDATED TIMESTAMP WITHOUT TIME ZONE ,
    UPDATED_BY CHARACTER VARYING COLLATE pg_catalog."default" ,
    CODE CHARACTER VARYING COLLATE pg_catalog."default" NOT NULL,
    NAME CHARACTER VARYING COLLATE pg_catalog."default" NOT NULL,
    TYPE CHARACTER VARYING COLLATE pg_catalog."default" NOT NULL,
    ICON CHARACTER VARYING COLLATE pg_catalog."default",
    CONSTRAINT GROUP_ID_PK PRIMARY KEY(ID),
    CONSTRAINT GROUP_CODE_UNQ UNIQUE (CODE),
    CONSTRAINT GROUP_NAME_UNQ UNIQUE (NAME)
);

INSERT INTO ORG_GROUP(CODE, NAME, TYPE) VALUES ('HQ', 'HQ Group', 'HQ');