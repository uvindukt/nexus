package com.nexus.inventory.application.service;

import com.nexus.inventory.domain.model.InboxEnvelope;

public interface InboxService {

    void onStockEvent(InboxEnvelope inboxEnvelope);

    void archive(Integer maxRetries, Integer batchSize);

}
