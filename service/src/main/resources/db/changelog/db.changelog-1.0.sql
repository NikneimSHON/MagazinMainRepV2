--liquibase formatted sql

--changeset tsar:1
CREATE TABLE users
(
    id                BIGSERIAL PRIMARY KEY,
    email             VARCHAR(128) UNIQUE NOT NULL,
    email_verified    BOOLEAN             NOT NULL,
    last_login        TIMESTAMP           NOT NULL,
    phone_number      VARCHAR(128) UNIQUE NOT NULL,
    password          VARCHAR(128)        NOT NULL,
    registration_date TIMESTAMP           NOT NULL,
    birth_date        DATE                NOT NULL,
    firstname        VARCHAR(128),
    lastname         VARCHAR(128),
    image             VARCHAR(128),
    activity          VARCHAR(128)        NOT NULL,
    role              VARCHAR(128)        NOT NULL
);

--changeset tsar:2
CREATE TABLE product
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(128) NOT NULL,
    price         INTEGER CHECK (price > 0) NOT NULL,
    create_at     TIMESTAMP NOT NULL,
    available     BOOLEAN,
    description   VARCHAR(128) NOT NULL,
    category      VARCHAR(128) NOT NULL,
    count         INT CHECK (count >= 0) NOT NULL,
    product_image VARCHAR(128)
);

--changeset tsar:3
CREATE TABLE address
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT REFERENCES users (id) NOT NULL,
    country          VARCHAR(128) NOT NULL,
    city             VARCHAR(128) NOT NULL,
    street           VARCHAR(128) NOT NULL,
    house_number     INT          NOT NULL,
    apartment_number INT
);

--changeset tsar:4
CREATE TABLE shopping_cart
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT REFERENCES users (id) NOT NULL,
    order_status VARCHAR(128) NOT NULL,
    create_time  TIMESTAMP NOT NULL,
    update_time  TIMESTAMP NOT NULL
);

--changeset tsar:5
CREATE TABLE shopping_cart_product
(
    id               BIGSERIAL PRIMARY KEY,
    shopping_cart_id BIGINT REFERENCES shopping_cart (id),
    product_id       BIGINT REFERENCES product (id),
    count_product    INT NOT NULL CHECK (count_product > 0)
);

--changeset tsar:6
CREATE TABLE product_review
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT REFERENCES product (id) NOT NULL,
    create_time TIMESTAMP NOT NULL,
    user_id     BIGINT REFERENCES users (id) NOT NULL,
    description VARCHAR(128),
    image       VARCHAR(128),
    rating      INT CHECK (rating BETWEEN 1 AND 5) NOT NULL
);

--changeset tsar:7
CREATE TABLE promo_code
(
    id                 BIGSERIAL PRIMARY KEY,
    code               VARCHAR(128) UNIQUE NOT NULL,
    discount_sum       INTEGER CHECK (discount_sum >= 0) NOT NULL,
    remaining_quantity INT CHECK (remaining_quantity >= 0) NOT NULL,
    activity_promo     BOOLEAN NOT NULL,
    min_order_amount   Integer CHECK (min_order_amount >= 0) NOT NULL,
    valid_to           TIMESTAMP NOT NULL,
    valid_from         TIMESTAMP NOT NULL
);

--changeset tsar:8
CREATE TABLE user_promo_code
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id) NOT NULL,
    promo_code_id BIGINT REFERENCES promo_code (id) NOT NULL,
    used_at       TIMESTAMP
);