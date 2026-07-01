package com.nexus.rag.domain.model.consumer;

import com.nexus.shared.common.InboxEventType;

public enum ProductEventType implements InboxEventType {
    PRODUCT_CREATED,
    PRODUCT_UPDATED
}
