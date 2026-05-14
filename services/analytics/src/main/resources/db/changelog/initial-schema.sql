-- liquibase formatted sql

-- 1. Inbox Table for Analytics Service Idempotency
CREATE TABLE IF NOT EXISTS inbox
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(255),
    aggregate_id   VARCHAR(255),
    type           VARCHAR(255),
    payload        JSONB,
    status         VARCHAR(50),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at   TIMESTAMPTZ
);

-- Index to speed up idempotency checks during Kafka consumption
CREATE INDEX IF NOT EXISTS idx_inbox_aggregate
    ON inbox (aggregate_type, aggregate_id);

-- 2. Inbox Archive Table (For the Transactional Archiving Pattern)
CREATE TABLE IF NOT EXISTS inbox_archive
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(255),
    aggregate_id   VARCHAR(255),
    type           VARCHAR(255),
    payload        JSONB,
    status         VARCHAR(50),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at   TIMESTAMPTZ
);

-- 3. Product Stock View (Read Projection for SSE Streaming)
CREATE TABLE IF NOT EXISTS product_stock_view
(
    product_id         BIGINT PRIMARY KEY,
    product_name       VARCHAR(255),
    slug               VARCHAR(255),
    sku                VARCHAR(100),
    price              NUMERIC(19, 2),
    status             VARCHAR(50),
    brand_name         VARCHAR(255),
    category_name      VARCHAR(255),
    available_quantity INTEGER DEFAULT 0,
    reserved_quantity  INTEGER DEFAULT 0,
    total_quantity     INTEGER DEFAULT 0,
    last_updated       TIMESTAMPTZ
);

-- Unique index for slug-based lookups from the UI/Web Controllers
CREATE UNIQUE INDEX IF NOT EXISTS idx_psv_slug ON product_stock_view (slug);