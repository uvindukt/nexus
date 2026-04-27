-- liquibase formatted sql

-- changeset Uvindu:1777301587646-4
ALTER TABLE product
    ADD sku VARCHAR(50);

-- changeset Uvindu:1777301587646-5
ALTER TABLE product
    ALTER COLUMN sku SET NOT NULL;

-- changeset Uvindu:1777301587646-6
ALTER TABLE product
    ADD CONSTRAINT uc_product_sku UNIQUE (sku);

-- changeset Uvindu:1777301587646-8
ALTER TABLE product
    DROP COLUMN code;
ALTER TABLE product
    DROP COLUMN version;

-- changeset Uvindu:1777301587646-1
ALTER TABLE brand
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777301587646-2
ALTER TABLE category
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777301587646-3
ALTER TABLE product_attribute
    ALTER COLUMN product_id SET NOT NULL;

