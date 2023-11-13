create table authorities (
    id bigint not null,
    name varchar(20) not null,
    primary key (id)
);
create table authorities_seq (
    next_val bigint
);
insert into authorities_seq values ( 1 );
create table roles (
    id bigint not null,
    name varchar(20) not null,
    primary key (id)
);
create table roles_authorities (
    roles_id bigint not null,
    authorities_id bigint not null
);
create table roles_seq (
    next_val bigint
);
insert into roles_seq values ( 1 );
create table users (
    id bigint not null,
    email varchar(120) not null,
    encrypted_password varchar(255) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    user_id varchar(255) not null,
    primary key (id)
);
create table users_roles (
    users_id bigint not null,
    roles_id bigint not null
);
create table users_seq (
    next_val bigint
);
insert into users_seq values ( 1 );
alter table users add constraint email_UK unique (email);
alter table users add constraint user_id_UK unique (user_id);
alter table users add constraint encrypted_password_UK unique (encrypted_password);
alter table roles_authorities add constraint authorities_roles_id_foreign_key foreign key (authorities_id) references authorities (id);
alter table roles_authorities add constraint roles_authorities_id_foreign_key foreign key (roles_id) references roles (id);
alter table users_roles add constraint roles_users_id_foreign_key foreign key (roles_id) references roles (id);
alter table users_roles add constraint users_roles_id_foreign_key foreign key (users_id) references users (id);