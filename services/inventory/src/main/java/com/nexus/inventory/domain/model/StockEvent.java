package com.nexus.inventory.domain.model;

public record StockEvent(
        Long id,
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
