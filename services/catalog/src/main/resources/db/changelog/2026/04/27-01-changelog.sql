-- liquibase formatted sql

-- changeset Uvindu:1777286567312-5
ALTER TABLE category
    DROP COLUMN is_active;

-- changeset Uvindu:1777286567312-1
ALTER TABLE brand
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777286567312-2
ALTER TABLE category
    ALTER COLUMN active SET NOT NULL;

-- changeset Uvindu:1777286567312-3
ALTER TABLE brand
    ALTER COLUMN logo_url SET NOT NULL;

-- changeset Uvindu:1777286567312-4
ALTER TABLE brand
    ALTER COLUMN website SET NOT NULL;

