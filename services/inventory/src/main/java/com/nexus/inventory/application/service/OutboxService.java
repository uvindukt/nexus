package com.nexus.inventory.application.service;

import com.nexus.inventory.domain.model.Stock;
import com.nexus.shared.outbox.OutboxEventType;

import java.time.Instant;

public interface OutboxService {
    
    /**
     * Records a stock-related event in the outbox.
     *
     * @param stock the stock aggregate
     * @param eventType the type of event to record
     */
    void stockEvent(Stock stock, OutboxEventType eventType);

}
