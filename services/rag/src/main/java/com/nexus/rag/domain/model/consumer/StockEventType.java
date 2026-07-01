package com.nexus.rag.domain.model.consumer;

import com.nexus.shared.common.InboxEventType;

public enum StockEventType implements InboxEventType {
    STOCK_CREATED,
    STOCK_UPDATED
}
