package com.nexus.rag.domain.model.consumer;

import java.math.BigDecimal;

public record ProductEvent(
        Long id,
        String name,
        String slug,
        String sku,
        BigDecimal price,
        String status,
        String brandName,
        String categoryName,
        String description
) {
}
