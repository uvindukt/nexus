-- liquibase formatted sql

-- changeset Uvindu:1777287254719-3
ALTER TABLE category
    DROP COLUMN is_active;

-- changeset Uvindu:1777287254719-1
ALTER TABLE brand
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777287254719-2
ALTER TABLE category
    ALTER COLUMN active SET NOT NULL;

