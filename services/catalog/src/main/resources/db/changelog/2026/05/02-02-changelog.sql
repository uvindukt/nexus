-- liquibase formatted sql

-- changeset Uvindu:1777716460835-1
ALTER TABLE outbox
    ADD last_attempted_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE outbox
    ADD retry_count INTEGER;

-- changeset Uvindu:1777716460835-3
ALTER TABLE outbox
    ALTER COLUMN retry_count SET NOT NULL;

