create table directive
(
    Name        TEXT                   not null
        constraint directive_pk
            primary key,
    "Default"   TEXT default null,
    Type        TEXT default 'string'  not null,
    Changelog   TEXT default null,
    Changable   TEXT default 'INI_ALL' not null,
    Description TEXT                   not null,
    MinVersion  TEXT default null,
    MaxVersion  TEXT default null
);

create index directive_Changable_IDX
    on directive (Changable);

create index directive_MinVersion_IDX
    on directive (MinVersion, MaxVersion);

create index directive_Type_IDX
    on directive (Type);

