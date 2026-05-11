package com.nexus.inventory.application.service;

import com.nexus.shared.common.InboxEnvelope;

public interface InboxService {

    /**
     * Processes product events received via the inbox envelope.
     *
     * @param inboxEnvelope the event envelope containing payload and metadata
     */
    void onProductEvent(InboxEnvelope inboxEnvelope);

    /**
     * Archives processed inbox messages to the archive repository.
     *
     * @param batchSize the number of records to process in a single batch
     * @return the number of records successfully archived
     */
    Integer archive(Integer batchSize);

}
