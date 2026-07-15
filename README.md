# Nexus

Nexus is an e-commerce platform accelerator built as an event driven Spring Boot microservices system, combining classic transactional patterns (outbox/inbox, CDC) with a Retrieval-Augmented Generation (RAG) service for natural-language product search.

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

Cross-cutting infrastructure: PostgreSQL (with pgvector), Qdrant (vector store), Kafka + Debezium (CDC), Redis, Keycloak (OAuth2/OIDC), Ollama (local embedding inference).

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=flat-square&logo=apachekafka&logoColor=white)](https://kafka.apache.org/)
[![Debezium](https://img.shields.io/badge/Debezium-FF6A00?style=flat-square&logo=debezium&logoColor=white)](https://debezium.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Qdrant](https://img.shields.io/badge/Qdrant-DC244C?style=flat-square&logo=qdrant&logoColor=white)](https://qdrant.tech/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![Ollama](https://img.shields.io/badge/Ollama-000000?style=flat-square&logo=ollama&logoColor=white)](https://ollama.com/)
[![Google Gemini](https://img.shields.io/badge/Google_Gemini-8E75EC?style=flat-square&logo=googlegemini&logoColor=white)](https://ai.google.dev/gemini-api)
[![Keycloak](https://img.shields.io/badge/Keycloak-2563EB?style=flat-square&logo=keycloak&logoColor=white)](https://www.keycloak.org/)
[![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-ai)
[![Apache Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Liquibase](https://img.shields.io/badge/Liquibase-205C90?style=flat-square&logo=liquibase&logoColor=white)](https://www.liquibase.org/)
[![Swagger UI](https://img.shields.io/badge/Swagger_UI-85EA2D?style=flat-square&logo=swagger&logoColor=black)](https://swagger.io/tools/swagger-ui/)

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
- **AI/RAG:** Spring AI 2.0, Google Gemini (chat), Qwen3-Embedding-8B (4096-dim embeddings) served locally via Ollama. Vector storage is polymorphic across two backends behind a common port/repository abstraction, with the active adapter injected via Spring's `@Qualifier`: **Qdrant** (gRPC, port 6334 — current default) and **pgvector** (HNSW index, cosine distance — kept in place as an existing, already-working implementation)
- **Persistence:** PostgreSQL (`pgvector/pgvector:pg16`), Qdrant, Liquibase migrations, Spring Data JPA
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

1. **Ingestion:** Kafka product/stock event → `product_stock_view` upsert → build `Document` (brand + category + description) → embed via Qwen3-Embedding-8B (Ollama) → upsert into the vector store adapter injected via `@Qualifier` (Qdrant by default, pgvector as an alternate backend).
2. **Query:** user message → query rewriting → vector similarity retrieval (top-K, cosine threshold) → context injection into a prompt template → Gemini chat completion → response, with conversation history persisted in Redis.

### Vector Store Abstraction

The vector store is accessed through a port defined in `domain.repository`, with two interchangeable adapters in `infrastructure.persistence`:

- **Qdrant adapter** — uses `QdrantClient` / `scrollAsync` with payload filtering; a `productId` payload index (type Keyword) is created via an `ApplicationRunner` with an idempotency check.
- **PgVector adapter** — uses Spring AI's `PgVectorStore` with an HNSW cosine index; kept in place from an earlier implementation rather than removed, since it was already written and working.

The active adapter is wired in via Spring's `@Qualifier`, keeping ingestion and query logic backend-agnostic and making it easy to swap between the two.

## Getting Started

Local infrastructure (Postgres, Qdrant, Kafka, Debezium Connect, Keycloak, Redis, Ollama) is defined in `compose.yml`. Each service reads shared configuration from the `config` server, which must be running before the others start; `discovery` should also be up so services can register with Eureka. Per-service datasource credentials, Kafka bindings, and other settings live under `services/config/src/main/resources/configurations/{common,dev,prod}`.

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
