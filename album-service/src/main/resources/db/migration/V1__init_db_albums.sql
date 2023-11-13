create table if not exists public.albums
(
    id          bigserial
        primary key,
    album_id    varchar(255),
    description varchar(255),
    name        varchar(255),
    user_id     varchar(255)
);

alter table public.albums
    owner to postgres;