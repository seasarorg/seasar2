create table DOCUMENT (
    FOLDER_ID integer not null,
    FILE_ID integer not null,
    DOCUMENT_ID integer not null,
    DOCUMENT_SIZE integer,
    NAME varchar(255),
    constraint DOCUMENT_PK primary key(FOLDER_ID, FILE_ID, DOCUMENT_ID)
);
