package com.nexus.rag.domain.model.consumer;

public record StockEvent(
        Long id,
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
