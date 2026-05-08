package com.nexus.catalog.domain.port.out;

import com.nexus.shared.OutboxEnvelope;

/**
 * Port interface for publishing outbox events.
 * Infrastructure adapters (e.g. Kafka) implement this contract.
 */
public interface OutboxPublisher {

    void publishOutbox(OutboxEnvelope outboxEnvelope, String key);

}
