-- liquibase formatted sql

-- changeset Uvindu:1783682696851-1
CREATE SEQUENCE IF NOT EXISTS attribute_seq START WITH 1 INCREMENT BY 100;

-- changeset Uvindu:1783682696851-2
CREATE SEQUENCE IF NOT EXISTS brand_seq START WITH 1 INCREMENT BY 20;

-- changeset Uvindu:1783682696851-3
CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 100;

-- changeset Uvindu:1783682696851-4
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 1000;

-- changeset Uvindu:1783682696851-5
CREATE TABLE brand
(
    id           BIGINT                      NOT NULL,
    name         VARCHAR(255)                NOT NULL,
    website      VARCHAR(255),
    logo_url     VARCHAR(255),
    active       BOOLEAN                     NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_brand PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-6
CREATE TABLE category
(
    id        BIGINT       NOT NULL,
    name      VARCHAR(255) NOT NULL,
    slug      VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    active    BOOLEAN      NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-7
CREATE TABLE outbox
(
    id                UUID         NOT NULL,
    aggregate_type    VARCHAR(255) NOT NULL,
    aggregate_id      VARCHAR(255) NOT NULL,
    type              VARCHAR(255) NOT NULL,
    payload           JSONB        NOT NULL,
    status            VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    processed_at      TIMESTAMP WITHOUT TIME ZONE,
    retry_count       INTEGER      NOT NULL,
    last_attempted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_outbox PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-8
CREATE TABLE outbox_archive
(
    id             UUID         NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id   VARCHAR(255) NOT NULL,
    type           VARCHAR(255) NOT NULL,
    payload        JSONB        NOT NULL,
    status         VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    processed_at   TIMESTAMP WITHOUT TIME ZONE,
    archived_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_outboxarchive PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-9
CREATE TABLE product
(
    id          BIGINT                      NOT NULL,
    sku         VARCHAR(50)                 NOT NULL,
    name        VARCHAR(255)                NOT NULL,
    slug        VARCHAR(255)                NOT NULL,
    description TEXT                        NOT NULL,
    price       DECIMAL(10, 2)              NOT NULL,
    status      VARCHAR(20)                 NOT NULL,
    brand_id    BIGINT,
    category_id BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-10
CREATE TABLE product_attribute
(
    id         BIGINT       NOT NULL,
    key        VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL,
    product_id BIGINT       NOT NULL,
    CONSTRAINT pk_product_attribute PRIMARY KEY (id)
);

-- changeset Uvindu:1783682696851-11
ALTER TABLE brand
    ADD CONSTRAINT uc_brand_name UNIQUE (name);

-- changeset Uvindu:1783682696851-12
ALTER TABLE brand
    ADD CONSTRAINT uc_brand_website UNIQUE (website);

-- changeset Uvindu:1783682696851-13
ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

-- changeset Uvindu:1783682696851-14
ALTER TABLE category
    ADD CONSTRAINT uc_category_slug UNIQUE (slug);

-- changeset Uvindu:1783682696851-15
ALTER TABLE product
    ADD CONSTRAINT uc_product_sku UNIQUE (sku);

-- changeset Uvindu:1783682696851-16
ALTER TABLE product
    ADD CONSTRAINT uc_product_slug UNIQUE (slug);

-- changeset Uvindu:1783682696851-18
CREATE INDEX idx_product_attribute_key ON product_attribute (key);

-- changeset Uvindu:1783682696851-22
CREATE INDEX idx_product_status ON product (status);

-- changeset Uvindu:1783682696851-23
ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_PARENT FOREIGN KEY (parent_id) REFERENCES category (id);
CREATE INDEX idx_category_parent_id ON category (parent_id);

-- changeset Uvindu:1783682696851-24
ALTER TABLE product_attribute
    ADD CONSTRAINT FK_PRODUCT_ATTRIBUTE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);
CREATE INDEX idx_product_attribute_product_id ON product_attribute (product_id);

-- changeset Uvindu:1783682696851-25
ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);
CREATE INDEX idx_product_brand_id ON product (brand_id);

-- changeset Uvindu:1783682696851-26
ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);
CREATE INDEX idx_product_category_id ON product (category_id);

-- changeset Uvindu:1783682696851-27
CREATE INDEX idx_outbox_pending ON outbox (created_at) WHERE status = 'PENDING';
CREATE INDEX idx_outbox_processed ON outbox (created_at) WHERE status = 'PROCESSED';