create table users
(
    id       SERIAL PRIMARY KEY,
    login    varchar unique not null,
    name     varchar        not null,
    password varchar        not null
);