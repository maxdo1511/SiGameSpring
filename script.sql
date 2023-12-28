create table users
(
    id       serial,
    username text not null
        constraint users_pk
            primary key,
    password text
);

alter table users
    owner to postgres;

create table games
(
    id           serial
        constraint games_pk
            primary key,
    author       text   not null
        constraint games_users_username_fk
            references users,
    name         text   not null,
    release_date bigint not null
);

alter table games
    owner to postgres;

create table round
(
    id     serial
        constraint round_pk
            primary key,
    gameid serial
        constraint round_games_id_fk
            references games,
    name   text not null
);

alter table round
    owner to postgres;

create table theme
(
    id      serial
        constraint theme_pk
            primary key,
    roundid serial
        constraint theme_round_id_fk
            references round,
    name    text not null
);

alter table theme
    owner to postgres;

create table question
(
    id                 serial,
    themeid            integer not null
        constraint question_theme_id_fk
            references theme,
    title              text    not null,
    answer             text    not null,
    question_type      text    not null,
    question_attribute text,
    cost               integer
);

alter table question
    owner to postgres;


