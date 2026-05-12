package com.nexus.analytics.domain.model;

public record StockEvent(
        Long id,
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
