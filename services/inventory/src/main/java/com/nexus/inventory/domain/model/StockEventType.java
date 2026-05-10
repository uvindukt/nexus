package com.nexus.inventory.domain.model;

import com.nexus.shared.outbox.OutboxEventType;

public enum StockEventType implements OutboxEventType {
    STOCK_INITIALIZED,
    STOCK_UPDATED
}
