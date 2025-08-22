create table brand
(
    state      enum ('OPENED', 'CLOSED') null,
    created_at datetime(6)               not null,
    deleted_at datetime(6)               null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6)               not null,
    name       varchar(255)              null
);

create table orders
(
    created_at datetime(6) not null,
    deleted_at datetime(6) null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6) not null
);

create table payment
(
    amount          decimal(38, 2)                    null,
    created_at      datetime(6)                       not null,
    deleted_at      datetime(6)                       null,
    id              bigint auto_increment
        primary key,
    updated_at      datetime(6)                       not null,
    user_id         bigint                            not null,
    order_id        bigint,
    dtype           varchar(31),
    method_type enum ('USER_POINT')               null,
    type            enum ('CANCEL', 'PAID', 'REFUND') null
);

create table product
(
    price        decimal(38, 2)                    null,
    brand_id     bigint                            not null,
    created_at   datetime(6)                       not null,
    deleted_at   datetime(6)                       null,
    displayed_at datetime(6)                       null,
    id           bigint auto_increment
        primary key,
    max_quantity bigint                            not null,
    stock        bigint                            not null,
    updated_at   datetime(6)                       not null,
    name         varchar(255)                      null,
    state        enum ('AVAILABLE', 'UNAVAILABLE') null
);

create table order_line
(
    position     int            null,
    price        decimal(38, 2) null,
    brand_id     bigint         null,
    created_at   datetime(6)    not null,
    deleted_at   datetime(6)    null,
    id           bigint auto_increment
        primary key,
    order_id     bigint         not null,
    product_id   bigint         null,
    quantity     bigint         null,
    updated_at   datetime(6)    not null,
    product_name varchar(255)   null
);

create table product_like
(
    created_at datetime(6) not null,
    deleted_at datetime(6) null,
    id         bigint auto_increment
        primary key,
    product_id bigint      null,
    updated_at datetime(6) not null,
    user_id    bigint      not null
);

create table product_signal
(
    created_at datetime(6) not null,
    deleted_at datetime(6) null,
    id         bigint auto_increment
        primary key,
    like_count bigint      not null,
    product_id bigint      not null,
    updated_at datetime(6) not null
);

create table user_point
(
    point      decimal(38, 2) null,
    created_at datetime(6)    not null,
    deleted_at datetime(6)    null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6)    not null,
    user_id    bigint         not null
);

create table users
(
    created_at datetime(6)  not null,
    deleted_at datetime(6)  null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6)  not null,
    birth      varchar(255) null,
    email      varchar(255) null,
    gender     varchar(255) null,
    name       varchar(255) null,
    username   varchar(255) null
);

