create table roles
(
    id                 varchar(255) not null
        primary key,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6),
    name               varchar(255)
);

alter table roles
    owner to rombenc;

create table transactions
(
    id               varchar(255) not null
        primary key,
    transaction_date timestamp(6)
);

alter table transactions
    owner to rombenc;

create table users
(
    id                 varchar(255) not null
        primary key,
    created_date       timestamp(6) not null,
    email              varchar(255)
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    firstname          varchar(255),
    last_modified_date timestamp(6),
    lastname           varchar(255),
    password           varchar(255),
    verification_code  varchar(255),
    verified           boolean      not null
);

alter table users
    owner to rombenc;

create table customers
(
    id           varchar(255) not null
        primary key,
    birth_date   date,
    email        varchar(255) not null
        constraint ukrfbvkrffamfql7cjmen8v976v
            unique,
    full_name    varchar(255),
    phone_number varchar(255) not null
        constraint uk6v6x92wb400iwh6unf5rwiim4
            unique,
    points       numeric(38),
    user_id      varchar(255) not null
        constraint ukeuat1oase6eqv195jvb71a93s
            unique
        constraint fkrh1g1a20omjmn6kurd35o3eit
            references users
);

alter table customers
    owner to rombenc;

create table addresses
(
    id          varchar(255) not null
        primary key,
    city        varchar(255),
    country     varchar(255),
    detail      varchar(255),
    postal_code numeric(38),
    street      varchar(255),
    customer_id varchar(255)
        constraint fkhrpf5e8dwasvdc5cticysrt2k
            references customers
);

alter table addresses
    owner to rombenc;

create table histories
(
    id               varchar(255) not null
        primary key,
    transaction_date timestamp(6),
    customer_id      varchar(255)
        constraint fklgmjb8xx0hak68y3xk0dogk80
            references customers
);

alter table histories
    owner to rombenc;

create table merchants
(
    id           varchar(255) not null
        primary key,
    created_at   date,
    email        varchar(255) not null
        constraint ukgx9y1yah4qdijdi45ow7nxvdr
            unique,
    full_name    varchar(255),
    phone_number varchar(255) not null
        constraint ukfiwyk1j40kjrvdrhej7ge7k13
            unique,
    user_id      varchar(255) not null
        constraint ukh9v6fyuu5mnk7p3qtlpta2947
            unique
        constraint fka759srj6ts95j9qh089b6gbei
            references users
);

alter table merchants
    owner to rombenc;

create table orders
(
    id          varchar(255) not null
        primary key,
    order_date  timestamp(6),
    status      varchar(255),
    total_price numeric(38, 2),
    customer_id varchar(255) not null
        constraint fkpxtb8awmi0dk6smoh2vp1litg
            references customers
);

alter table orders
    owner to rombenc;

create table payments
(
    id             varchar(255) not null
        primary key,
    amount         numeric(38, 2),
    payment_date   timestamp(6),
    payment_method varchar(255),
    status         varchar(255),
    order_id       varchar(255) not null
        constraint fk81gagumt0r8y3rmudcgpbk42l
            references orders
);

alter table payments
    owner to rombenc;

create table products
(
    id           varchar(255) not null
        primary key,
    brand        varchar(255),
    created_at   timestamp(6) not null,
    description  varchar(255),
    last_update  timestamp(6),
    name         varchar(255),
    price        numeric(38, 2),
    product_code varchar(255) not null
        constraint uk922x4t23nx64422orei4meb2y
            unique,
    size         varchar(255)
        constraint products_size_check
            check ((size)::text = ANY
        ((ARRAY ['XXS'::character varying, 'XS'::character varying, 'S'::character varying, 'M'::character varying, 'L'::character varying, 'XL'::character varying, 'XXL'::character varying])::text[])),
    stock        integer,
    merchant_id  varchar(255)
        constraint fkt1yvv81v320ba41fq28k7had2
            references merchants
);

alter table products
    owner to rombenc;

create table carts
(
    id          varchar(255) not null
        primary key,
    price       numeric(38, 2),
    quantity    integer      not null,
    customer_id varchar(255)
        constraint fk8ba3sryid5k8a9kidpkvqipyt
            references customers,
    order_id    varchar(255)
        constraint fk1scg0ylakb1pltk7ivjpkaa57
            references orders,
    product_id  varchar(255) not null
        constraint fkmd2ap4oxo3wvgkf4fnaye532i
            references products
);

alter table carts
    owner to rombenc;

create table users_roles
(
    users_id varchar(255) not null
        constraint fkml90kef4w2jy7oxyqv742tsfc
            references users,
    roles_id varchar(255) not null
        constraint fka62j07k5mhgifpp955h37ponj
            references roles
);

alter table users_roles
    owner to rombenc;

