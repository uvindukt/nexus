-- liquibase formatted sql

-- changeset Uvindu:rag-001-01 labels:init
-- comment: pgvector extension (already enabled at DB level via init script, idempotent here)
CREATE EXTENSION IF NOT EXISTS vector;

-- changeset Uvindu:rag-001-02 labels:init
-- comment: Spring AI vector store table
CREATE TABLE vector_store (
                              id        UUID                        NOT NULL DEFAULT gen_random_uuid(),
                              content   TEXT,
                              metadata  JSON,
                              embedding vector(768),
                              CONSTRAINT pk_vector_store PRIMARY KEY (id)
);

-- changeset Uvindu:rag-001-03 labels:init
-- comment: HNSW index for approximate nearest-neighbour search (cosine)
CREATE INDEX idx_vector_store_embedding
    ON vector_store
        USING hnsw (embedding vector_cosine_ops);