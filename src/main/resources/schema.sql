create table if not exists CITIES
(
    id                serial primary key,
    name              varchar(100) not null,
    country           varchar(255),
    pop               bigint,
    version           bigint,
    created_by        varchar(200),
    creation_date     timestamp,
    modification_date timestamp,
    modified_by       varchar(200)
);