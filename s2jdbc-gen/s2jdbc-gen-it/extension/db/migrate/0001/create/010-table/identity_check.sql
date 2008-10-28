create table IDENTITY_CHECK (
    AAA boolean not null,
    BBB char(1) not null,
    CCC smallint not null,
    DDD smallint not null,
    EEE integer not null,
    FFF bigint not null,
    GGG float not null,
    HHH double not null,
    III varchar(255) not null,
    JJJ decimal not null,
    KKK bigint not null,
    LLL date not null,
    MMM date not null,
    NNN time not null,
    PPP timestamp not null,
    constraint IDENTITY_CHECK_PK primary key(AAA, BBB, CCC, DDD, EEE, FFF, GGG, HHH, III, JJJ, KKK, LLL, MMM, NNN, PPP)
);
