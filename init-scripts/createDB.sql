create table chats
(
    chat_id bigint generated always as identity
        constraint chats_pk
            primary key,
    name    varchar(30) not null
);

alter table chats
    owner to postgres;

create table roles
(
    role_id bigint generated always as identity
        constraint roles_pk
            primary key,
    name    varchar(30) not null
        constraint roles_pk_2
            unique
);

alter table roles
    owner to postgres;

create table users
(
    user_id  bigserial
        primary key,
    email    varchar(255) not null,
    password varchar(255) not null,
    name     varchar(255) not null,
    role     bigint       not null
        constraint users_roles_fk
            references roles
);

alter table users
    owner to postgres;

create table messages
(
    message_id bigserial
        primary key,
    content    varchar(255) not null,
    timestamp  varchar(255) not null,
    creator    bigint       not null
        constraint chats_users_fk
            references users,
    chat_id    bigint       not null
        constraint messages_chats_fk
            references chats
);

alter table messages
    owner to postgres;

create table chats_users
(
    chat_id bigint not null
        constraint chats_users_fk
            references chats,
    user_id bigint not null
        constraint users_chats_fk
            references users
);

alter table chats_users
    owner to postgres;

