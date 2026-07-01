package com.nexus.rag.application.service;

import com.nexus.rag.domain.model.consumer.Inbox;

public interface InboxAuditService {

    /**
     * Marks an inbox message as failed and updates its processing timestamp.
     *
     * @param inbox The inbox entity to be updated.
     */
    void saveAsFailed(Inbox inbox);

}
