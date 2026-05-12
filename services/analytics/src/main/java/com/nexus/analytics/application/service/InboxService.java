package com.nexus.analytics.application.service;

import com.nexus.shared.common.InboxEnvelope;
import reactor.core.publisher.Mono;

public interface InboxService {

    /**
     * Processes product-related events received from the inbox.
     *
     * @param inboxEnvelope The envelope containing the product event type and payload.
     * @return A Mono that completes when the event processing is finished.
     */
    Mono<Void> onProductEvent(InboxEnvelope inboxEnvelope);

    /**
     * Processes stock-related events received from the inbox.
     *
     * @param inboxEnvelope The envelope containing the stock event type and payload.
     * @return A Mono that completes when the event processing is finished.
     */
    Mono<Void> onStockEvent(InboxEnvelope inboxEnvelope);

}
