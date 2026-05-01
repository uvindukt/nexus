package com.nexus.catalog.domain.port;

import com.nexus.catalog.application.dto.messaging.v1.OutboxEvent;

/**
 * Port interface for publishing outbox events.
 * Infrastructure adapters (e.g. Kafka) implement this contract.
 */
public interface OutboxPublisherPort {

    void publishOutbox(OutboxEvent outboxEvent, String key);

}
