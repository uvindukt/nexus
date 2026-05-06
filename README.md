Consumer side — actual Kafka DLQ
When your inventory service (or any consumer) is implemented, Spring Cloud Stream handles DLQ natively. Add this to the consumer's application.yaml:
yamlspring:
cloud:
stream:
kafka:
bindings:
product-in-0:
consumer:
enable-dlq: true
dlq-name: product.dlq
dlq-partitions: 1
max-attempts: 3
back-off-initial-interval: 1000
back-off-multiplier: 2.0
back-off-max-interval: 10000
This means: try 3 times with exponential backoff, then route the failed message to product.dlq instead of crashing. Spring Cloud Stream handles all of this automatically — no code changes needed on the consumer.

Catalog Service (The "What" & "Price")

Responsibility: Manages the product's identity, description, and base price.

Outbox Events: PRODUCT_CREATED, PRICE_CHANGED.

Inventory Service (The "How Many")

Responsibility: Tracks stock levels across warehouses and manages "low stock" thresholds.

Outbox Events: STOCK_UPDATED, OUT_OF_STOCK.

Order Service (The "Transaction")

Responsibility: Orchestrates the checkout process. It "locks" inventory and calculates the final total.

Outbox Events: ORDER_PLACED, ORDER_COMPLETED.

Notification Service (The "Messenger")

Responsibility: Sends emails, SMS, or push notifications based on Kafka events.

Logic: A "Sink" service—it consumes events but usually doesn't need its own Outbox.

Analytics Service (The "Brain")

Responsibility: Consumes every single event to build dashboards and sales reports.

Storage: Likely uses a different DB (like ClickHouse) optimized for huge read queries.