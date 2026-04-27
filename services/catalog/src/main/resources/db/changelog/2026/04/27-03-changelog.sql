-- liquibase formatted sql

-- changeset Uvindu:1777293399375-1
ALTER TABLE brand
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777293399375-2
ALTER TABLE category
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777293399375-3
ALTER TABLE product_attribute
    ALTER COLUMN product_id SET NOT NULL;

