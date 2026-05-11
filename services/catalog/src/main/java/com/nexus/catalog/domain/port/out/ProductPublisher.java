package com.nexus.catalog.domain.port.out;

import com.nexus.shared.common.OutboxEnvelope;

/**
 * Port interface for publishing product events.
 * Infrastructure adapters (e.g. Kafka) implement this contract.
 */
public interface ProductPublisher {

    void publishProduct(OutboxEnvelope outboxEnvelope, String key);

}
