package com.nexus.catalog.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.Product;

import java.time.Instant;

public interface OutboxService {

    /**
     * Fetch pending messages and publish
     *
     * @param product Created {@link Product}
     * @return {@link Outbox}
     * @throws JsonProcessingException When failed to convert {@link Product} to JSON
     */
    Outbox publish(Product product) throws JsonProcessingException;

    /**
     * Archives a set number of entries from Outbox table to improve performance
     *
     * @param threshold Time threshold for the entries to be archived
     * @param limit     Number of entries to be archived
     * @return {@link Integer} Number of entries archived
     */
    Integer archive(Instant threshold, Integer limit);

}
