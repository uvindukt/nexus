-- liquibase formatted sql

-- changeset Uvindu:1778930464501-1
CREATE TABLE inbox
(
    id             UUID         NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id   VARCHAR(255) NOT NULL,
    type           VARCHAR(255) NOT NULL,
    payload        JSONB        NOT NULL,
    status         VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    processed_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_inbox PRIMARY KEY (id)
);

-- changeset Uvindu:1778930464501-2
CREATE TABLE inbox_archive
(
    id             UUID                        NOT NULL,
    aggregate_type VARCHAR(255)                NOT NULL,
    aggregate_id   VARCHAR(255)                NOT NULL,
    type           VARCHAR(255)                NOT NULL,
    payload        JSONB                       NOT NULL,
    status         VARCHAR(255)                NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    processed_at   TIMESTAMP WITHOUT TIME ZONE,
    archived_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_inboxarchive PRIMARY KEY (id)
);

-- changeset Uvindu:1778930464501-3
CREATE TABLE product_stock_view
(
    product_id         BIGINT NOT NULL,
    product_name       VARCHAR(255),
    slug               VARCHAR(255),
    sku                VARCHAR(255),
    price              DECIMAL,
    status             VARCHAR(255),
    brand_name         VARCHAR(255),
    category_name      VARCHAR(255),
    available_quantity INTEGER,
    reserved_quantity  INTEGER,
    total_quantity     INTEGER,
    last_updated       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_productstockview PRIMARY KEY (product_id)
);

-- Unique index for slug-based lookups from the UI/Web Controllers
CREATE UNIQUE INDEX IF NOT EXISTS idx_psv_slug ON product_stock_view (slug);