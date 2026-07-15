-- liquibase formatted sql

-- changeset Uvindu:1784111649975-2
ALTER TABLE product_stock_view
    ADD retries INTEGER;

-- changeset Uvindu:1784111649975-3
ALTER TABLE product_stock_view
    ALTER COLUMN retries SET NOT NULL;

