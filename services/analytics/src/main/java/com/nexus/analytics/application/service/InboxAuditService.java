package com.nexus.analytics.application.service;

import com.nexus.analytics.domain.model.Inbox;

public interface InboxAuditService {

    /**
     * Marks an inbox message as failed and updates its processing timestamp.
     *
     * @param inbox The inbox entity to be updated.
     */
    void saveAsFailed(Inbox inbox);

}
