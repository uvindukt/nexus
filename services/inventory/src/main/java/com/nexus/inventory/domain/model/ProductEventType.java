package com.nexus.inventory.domain.model;

import com.nexus.shared.common.InboxEventType;

public enum ProductEventType implements InboxEventType {
    PRODUCT_CREATED,
    PRODUCT_UPDATED
}
