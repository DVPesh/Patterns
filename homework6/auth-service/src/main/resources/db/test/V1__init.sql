CREATE TABLE IF NOT EXISTS visitors
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    username   varchar(100) NOT NULL,
    password   varchar(255) NOT NULL,
    email      varchar(50)       DEFAULT NULL,
    created_at timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY username_UNIQUE (username),
    UNIQUE KEY password_UNIQUE (password),
    UNIQUE KEY email_UNIQUE (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица пользователей';

CREATE TABLE IF NOT EXISTS roles
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    name       varchar(50) NOT NULL,
    created_at timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY name_UNIQUE (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица ролей';

CREATE TABLE IF NOT EXISTS visitors_roles
(
    visitor_id bigint    NOT NULL,
    role_id    bigint    NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (visitor_id, role_id),
    KEY fk_roles_idx (role_id),
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_visitors FOREIGN KEY (visitor_id) REFERENCES visitors (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='таблица ролей пользователей';

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into visitors (username, password, email)
values ('Dima Petrov', '$2a$12$kHYybMz2P4hCtq7GW.wf3.LamFg338C48zvPdbudyzmxCN8LuQCei', 'dima@mail.ru'),
       ('Oleg Ivanov', '$2a$12$G628fF.N.y/xcEUhUvF67u0nLfmeenasXnVaDG/ita2pgsCxIsQWK', 'oleg@mail.ru'),
       ('Ann Kuskova', '$2a$12$zAY4wh7CXHkveqvPTUTuuOXql5tHYbt12V3ePphiU8EoSV38o8m4G', 'ann@mail.ru'),
       ('Peter Vetrov', '$2a$12$myTKY4SMKfZ/6ldoUYYh7uqLt4Y8XgPtz1JbuYRHbGWbpKF3tEyte', 'peter@mail.ru');

insert into visitors_roles (visitor_id, role_id)
values (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);
