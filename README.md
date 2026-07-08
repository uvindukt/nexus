# Nexus

Nexus is an e-commerce platform built as a Spring Boot microservices system, combining classic transactional patterns (outbox/inbox, CDC) with a Retrieval-Augmented Generation (RAG) service for natural-language product search.

## Architecture

```
                        ┌─────────────┐
                        │   Gateway   │  (API Gateway)
                        └──────┬──────┘
                               │
        ┌──────────────┬──────┴───────┬──────────────┐
        │              │              │              │
  ┌───────────┐  ┌────────────┐  ┌───────────┐  ┌───────────┐
  │  Catalog  │  │  Inventory │  │ Analytics │  │    RAG    │
  └─────┬─────┘  └──────┬─────┘  └─────┬─────┘  └─────┬─────┘
        │               │              │              │
        │  Kafka (outbox / CDC events) │              │
        └───────────────┴──────────────┴──────────────┘

  ┌────────────┐   ┌────────────┐
  │ Discovery  │   │   Config   │   (Eureka + centralized Config Server)
  │  (Eureka)  │   │   Server   │
  └────────────┘   └────────────┘
```

Cross-cutting infrastructure: PostgreSQL (with pgvector), Kafka + Debezium (CDC), Redis, Keycloak (OAuth2/OIDC).

## Services

| Service | Responsibility | Port |
|---|---|---|
| `discovery` | Eureka service registry | 8761 |
| `config` | Centralized Spring Cloud Config Server | 8888 |
| `gateway` | API Gateway / routing entrypoint | *(not yet configured)* |
| `catalog` | Product, brand, and category management. Owns product data and publishes product events via the transactional outbox pattern | 8081 |
| `inventory` | Stock management. Consumes product-created events, tracks available/reserved quantity, publishes stock events via Debezium CDC on its outbox table | 8082 |
| `analytics` | Builds a denormalized product+stock read view from Kafka events and streams live updates to clients over SSE (via Redis pub/sub) | 8084 |
| `rag` | Conversational product search. Ingests product+stock data into a vector store and answers natural-language queries using Retrieval-Augmented Generation | 8085 |
| `shared-lib` | Shared JPA/R2DBC base classes and common DTOs (`InboxEnvelope`, `OutboxEnvelope`, event type interfaces) used across services | — |

## Tech Stack

- **Framework:** Spring Boot 4, Spring Cloud (Config, Eureka, Gateway, Stream/Kafka)
- **AI/RAG:** Spring AI 2.0, Google Gemini (chat) + `text-embedding-004` (768-dim embeddings), pgvector with HNSW index (cosine distance)
- **Persistence:** PostgreSQL (`pgvector/pgvector:pg16`), Liquibase migrations, Spring Data JPA
- **Messaging:** Apache Kafka, Debezium CDC (logical replication via `pgoutput`)
- **Caching / Memory:** Redis (product cache in `catalog`, SSE pub/sub in `analytics`, chat conversation memory in `rag`)
- **Auth:** Keycloak (OAuth2 resource server / JWT)
- **Build:** Maven (multi-module for `catalog`, `inventory`, `analytics`, `rag`, `shared-lib`; `discovery`, `config`, `gateway` are standalone modules)
- **Docs:** springdoc-openapi / Swagger UI per service

## Event-Driven Data Flow

Nexus uses two flavors of the outbox pattern side by side:

- **`catalog`** writes to an `outbox` table inside the same transaction as product changes, then a scheduled poller (`OutboxScheduler`) publishes pending rows to Kafka via Spring Cloud Stream.
- **`inventory`** writes to its own `outbox` table but relies on **Debezium CDC** to tail the Postgres write-ahead log and publish changes directly to Kafka — no polling required.

Downstream services (`inventory`, `analytics`, `rag`) consume these events through an **inbox** pattern for idempotency (deduplicated by message ID) and route product/stock events to their own domain logic:

- `inventory` creates a stock record when a product is created.
- `analytics` maintains a `product_stock_view` and broadcasts live updates over SSE.
- `rag` maintains its own `product_stock_view`, computes a content hash per product, and re-embeds into the vector store only when the hash changes (avoiding redundant embedding calls). Deletion before re-insertion is done by filtering on `productId` metadata, not by vector similarity.

## RAG Search Pipeline

1. **Ingestion:** Kafka product/stock event → `product_stock_view` upsert → build `Document` (brand + category + description) → embed → upsert into `vector_store`.
2. **Query:** user message → query rewriting → vector similarity retrieval (top-K, cosine threshold) → context injection into a prompt template → Gemini chat completion → response, with conversation history persisted in Redis.

## Getting Started

Local infrastructure (Postgres, Kafka, Debezium Connect, Keycloak, Redis) is defined in `compose.yml`. Each service reads shared configuration from the `config` server, which must be running before the others start; `discovery` should also be up so services can register with Eureka. Per-service datasource credentials, Kafka bindings, and other settings live under `services/config/src/main/resources/configurations/{common,dev,prod}`.

## Project Structure

```
services/
  shared-lib/    # shared JPA/R2DBC base classes, common DTOs
  discovery/     # Eureka server
  config/        # Spring Cloud Config server
  gateway/       # API gateway
  catalog/       # product/brand/category service
  inventory/     # stock service
  analytics/     # read-model + SSE service
  rag/           # RAG search service
```