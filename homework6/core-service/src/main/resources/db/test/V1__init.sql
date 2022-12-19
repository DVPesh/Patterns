CREATE TABLE IF NOT EXISTS categories
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    title      varchar(255) NOT NULL,
    created_at timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица категорий продуктов';

CREATE TABLE IF NOT EXISTS products
(
    id          bigint        NOT NULL AUTO_INCREMENT,
    title       varchar(255)  NOT NULL,
    price       decimal(8, 2) NOT NULL,
    category_id bigint        NOT NULL,
    created_at  timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY fk_product_category_idx (category_id),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица продуктов';

INSERT INTO categories (title)
values ('молочные продукты'),
       ('электроника'),
       ('детские товары');

INSERT INTO products (title, price, category_id)
values ('кефир', 110.5, 1),
       ('молоко', 82.32, 1),
       ('ряженка', 130.87, 1),
       ('йогурт', 150.32, 1),
       ('музыкальный центр', 25100.12, 2),
       ('ноутбук', 120000, 2),
       ('мишка плюшевый', 2000.12, 3),
       ('паровоз', 500.32, 3);

CREATE TABLE IF NOT EXISTS orders
(
    id         bigint        NOT NULL AUTO_INCREMENT,
    username   varchar(100)  NOT NULL,
    total_cost decimal(8, 2) NOT NULL,
    address    varchar(255)       DEFAULT NULL,
    phone      varchar(50)        DEFAULT NULL,
    created_at timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица заказов';

CREATE TABLE IF NOT EXISTS order_items
(
    id                bigint        NOT NULL AUTO_INCREMENT,
    product_id        bigint        NOT NULL,
    order_id          bigint        NOT NULL,
    quantity          int           NOT NULL,
    price_per_product decimal(8, 2) NOT NULL,
    cost              decimal(8, 2) NOT NULL,
    created_at        timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY fk_products_idx (product_id),
    KEY fk_orders_idx (order_id),
    CONSTRAINT fk_orders FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_products FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица содержимого заказов';
