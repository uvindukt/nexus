package com.nexus.inventory.domain.model;

import com.nexus.shared.common.OutboxEventType;

public enum StockEventType implements OutboxEventType {
    STOCK_CREATED,
    STOCK_UPDATED
}
