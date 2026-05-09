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

    /**
     * Archives processed outbox records older than the threshold.
     *
     * @param threshold the cutoff time for archiving
     * @param limit the maximum number of records to archive in one batch
     * @return the number of records archived
     */
    Integer archive(Instant threshold, Integer limit);

}
