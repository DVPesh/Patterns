CREATE TABLE IF NOT EXISTS products (
    id              bigint          NOT NULL AUTO_INCREMENT,
    cost            decimal(8,2)    NOT NULL,
    expiration_date date            DEFAULT NULL,
    quantity        bigint          NOT NULL,
    supplier_email  varchar(255)    DEFAULT NULL,
    title           varchar(255)    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO products VALUES
    (7,75.00,'2022-09-21',25,'supplier1@yandex.ru','огурцы'),
    (8,98.00,'2022-10-01',17,'supplier2@yandex.ru','редис'),
    (13,85.00,NULL,38,'supplier3@gmail.ru','мыло'),
    (14,15400.00,NULL,12,'','гитара'),
    (15,2500.00,NULL,47,'omega@mail.ru','наушники'),
    (16,150.00,'2022-09-30',80,'supplier1@yandex.ru','помидоры'),
    (17,435.00,'2022-10-01',78,'supplier3@gmail.ru','морская капуста'),
    (18,110.00,'2022-10-05',45,'supplier1@yandex.ru','слива'),
    (19,355.00,'2022-10-02',56,'oyster@yandex.ru','устрицы'),
    (20,312.00,'2022-09-17',12,'chicken@gmail.com','курица'),
    (21,85.00,'2022-09-09',16,'supplier1@yandex.ru','бананы'),
    (22,1100.00,'2022-10-01',11,'fish@yandex.ru','форель'),
    (23,45.00,'2022-09-19',56,'supplier3@gmail.ru','укроп'),
    (24,830.00,'2022-09-19',23,'chicken@gmail.com','говядина'),
    (25,39700.00,NULL,2,'supplier3@gmail.ru','лодка надувная'),
    (26,350.00,NULL,25,'chicken@gmail.com','пластиковый контейнер'),
    (27,78.00,NULL,54,'supplier1@yandex.ru','зубочистки'),
    (28,289.00,'2022-12-09',36,'supplier2@yandex.ru','конфеты'),
    (29,250.00,'2022-11-05',41,'supplier3@gmail.ru','печенье'),
    (30,4206.00,NULL,6,'chicken@gmail.com','чайник'),
    (33,43.00,'2022-09-07',29,'supplier3@gmail.ru','хлеб');
