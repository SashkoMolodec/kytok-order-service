CREATE TABLE orders
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    vinyl_id           BIGSERIAL             NOT NULL,
    vinyl_title        varchar(255),
    vinyl_author       varchar(255),
    vinyl_price        float8,
    quantity           int                   NOT NULL,
    order_status       varchar(255)          NOT NULL,
    created_date       timestamp             NOT NULL,
    last_modified_date timestamp             NOT NULL,
    version            integer               NOT NULL
);
