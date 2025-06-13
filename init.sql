create table post (
    id serial primary key,
    title text,
    link text unique,
    description text,
    time timestamp
);