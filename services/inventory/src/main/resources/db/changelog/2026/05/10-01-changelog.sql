-- liquibase formatted sql

-- changeset Uvindu:1778430207819-1
DROP TABLE outbox_archive CASCADE;

-- changeset Uvindu:1778430207819-7
ALTER TABLE outbox
    DROP COLUMN last_attempted_at;
ALTER TABLE outbox
    DROP COLUMN processed_at;
ALTER TABLE outbox
    DROP COLUMN retry_count;
ALTER TABLE outbox
    DROP COLUMN status;

