--liquibase formatted sql

--changeset Emelyanov535:001-Добавление поля isAvailable в таблицу t_product
ALTER TABLE t_product ADD COLUMN is_available BOOLEAN NOT NULL DEFAULT TRUE;

--changeset Emelyanov535:002-Создание последовательности customer_sequence
CREATE SEQUENCE customer_sequence START WITH 1 INCREMENT BY 1;

--changeset Emelyanov535:003-Создание таблицы t_customer
CREATE TABLE t_customer (
    id BIGINT PRIMARY KEY DEFAULT nextval('customer_sequence'),
    login VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL
);

--changeset Emelyanov535:004-Создание таблицы t_order
CREATE TABLE t_order (
     id UUID PRIMARY KEY,
     status VARCHAR(50) NOT NULL,
     delivery_address VARCHAR(255) NOT NULL,
     customer BIGINT NOT NULL,
     CONSTRAINT fk_customer FOREIGN KEY (customer) REFERENCES t_customer(id)
);

--changeset Emelyanov535:005-Создание таблицы t_order_product с составным ключом
CREATE TABLE t_order_product (
     order_id UUID NOT NULL,
     product_id UUID NOT NULL,
     quantity INT NOT NULL,
     price DECIMAL(10, 2) NOT NULL,
     PRIMARY KEY (order_id, product_id),
     CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES t_order(id),
     CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES t_product(id)
);