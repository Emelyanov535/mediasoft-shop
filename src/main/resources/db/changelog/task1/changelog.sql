--liquibase formatted sql

--changeset Emelyanov535:create-product
CREATE TABLE t_product (
       id UUID PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       article BIGINT NOT NULL UNIQUE,
       description VARCHAR(255),
       category VARCHAR(255),
       price DECIMAL(10, 2) NOT NULL,
       amount INT NOT NULL,
       changed_amount TIMESTAMP,
       created_at DATE
);
