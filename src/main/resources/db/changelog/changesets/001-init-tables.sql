create table card_credential
(
    id          uuid         primary key,
    cvv         smallint     not null,
    end_date    timestamp(6) not null,
    card_number varchar(16)  not null,
    owner_id    uuid         not null
);

create table community_group
(
    id       uuid         primary key,
    owner_id uuid         not null,
    name     varchar(255) not null
);

create table promotion_task
(
    id          uuid         primary key ,
    is_approved boolean      not null,
    type        varchar(255) not null,
    group_id    uuid         not null,
    body        varchar(255) not null,
    subject     varchar(255) not null,
    image_name  varchar(128),
    constraint promotion_task_type_enum_value_constr check ( type in ('ORDERS', 'VIEWS', 'SUBSCRIBERS') )
);

create table subscription
(
    id         uuid         primary key ,
    date_end   timestamp(6) not null,
    date_start timestamp(6) not null,
    is_paid    boolean      not null default false,
    owner_id   uuid         not null,
    tariff_id  uuid         not null
);

create table tariff
(
    id                 uuid         primary key,
    price              float(53)    not null check (price >= 0),
    group_id           uuid         not null,
    name               varchar(255) not null,
    preview_image_name varchar(128),
    recipient_card_id  uuid         not null,
    constraint tariff_price_min_value_constr check ( price >= 0 )
);

create table transaction
(
    id                 uuid primary key,
    amount             float(53)    not null check (amount >= 0),
    created            timestamp(6) not null,
    state              varchar(64)  not null,
    payer_id           uuid         not null,
    payer_card_id      uuid         not null,
    recipient_card_id  uuid         not null,
    transaction_type   varchar(255) not null,
    target_entity_id   uuid,
    constraint transaction_transaction_type_enum_value_constr check ( transaction_type in ('DEBIT', 'WITHDRAW') ),
    constraint transaction_transaction_type_and_target_entity_id_unique_constr unique (transaction_type, target_entity_id)
);

create table goal
(
    id                 uuid         primary key,
    name               varchar(128) not null,
    target_sum         float        not null,
    current_sum        float        not null,
    group_id           uuid         not null,
    version            integer      not null,
    recipient_card_id  uuid,
    constraint goal_target_sum_min_value_constr check ( goal.target_sum >= 0 ),
    constraint goal_current_sum_min_value_constr check ( goal.current_sum >= 0 ),
    constraint goal_version_value_constr check ( goal.version >= 0 )
);

create table user_authorities
(
    role_id   uuid not null,
    authority varchar(255) unique not null
);

create table person
(
    id         uuid         primary key,
    first_name varchar(255) not null,
    password   varchar(255) not null,
    surname    varchar(255) not null
);

create table user_role
(
    id   uuid         primary key,
    name varchar(255) not null unique
);

create table user_to_group
(
    group_id uuid not null,
    user_id  uuid not null
);

create table user_to_role
(
    role_id uuid not null,
    user_id uuid not null
);
