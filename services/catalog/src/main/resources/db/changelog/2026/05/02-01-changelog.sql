-- liquibase formatted sql

-- changeset Uvindu:1777662592202-1
ALTER TABLE outbox_archive
    ALTER COLUMN archived_at DROP NOT NULL;

-- changeset Uvindu:1777662592202-2
ALTER TABLE outbox
    ALTER COLUMN created_at DROP NOT NULL;

-- changeset Uvindu:1777662592202-3
ALTER TABLE outbox_archive
    ALTER COLUMN created_at DROP NOT NULL;

-- changeset Uvindu:1777662592202-4
ALTER TABLE product
    ALTER COLUMN description SET NOT NULL;

-- changeset Uvindu:1777662592202-5
ALTER TABLE outbox
    ALTER COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

-- changeset Uvindu:1777662592202-6
ALTER TABLE outbox_archive
    ALTER COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

-- changeset Uvindu:1777662592202-7
ALTER TABLE outbox
    ALTER COLUMN type TYPE VARCHAR(255) USING (type::VARCHAR(255));

-- changeset Uvindu:1777662592202-8
ALTER TABLE outbox_archive
    ALTER COLUMN type TYPE VARCHAR(255) USING (type::VARCHAR(255));

-- changeset Uvindu:1777662592202-9
ALTER TABLE product
    ALTER COLUMN updated_at DROP NOT NULL;

