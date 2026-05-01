package com.nexus.catalog.application.service;

import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.Product;

import java.time.Instant;

public interface OutboxService {

    /**
     * Saves an {@link Outbox} entity related to the provided {@link Product}
     *
     * @param product Product object, which is the payload
     * @return {@link Product}
     */
    Outbox save(Product product);

    /**
     * Publish a single {@link Outbox}
     *
     * @param outbox Outbox entity
     */
    void publishSingle(Outbox outbox);

    /**
     * Archives a set number of entries from Outbox table to improve performance
     *
     * @param threshold Time threshold for the entries to be archived
     * @param limit     Number of entries to be archived
     * @return {@link Integer} Number of entries archived
     */
    Integer archive(Instant threshold, Integer limit);

}
