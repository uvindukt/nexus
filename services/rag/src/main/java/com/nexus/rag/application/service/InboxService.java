package com.nexus.rag.application.service;

import com.nexus.shared.common.InboxEnvelope;

public interface InboxService {

    /**
     * Processes product-related events received from the inbox.
     *
     * @param inboxEnvelope The envelope containing the product event type and payload.
     */
    void onProductEvent(InboxEnvelope inboxEnvelope);

    /**
     * Processes stock-related events received from the inbox.
     *
     * @param inboxEnvelope The envelope containing the stock event type and payload.
     */
    void onStockEvent(InboxEnvelope inboxEnvelope);

}
