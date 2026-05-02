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