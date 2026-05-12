package com.nexus.analytics.application.dto.web.response.v1;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.time.Instant;

@NullMarked
public record ProductStockViewResponse(
        Long productId,
        String productName,
        String slug,
        String sku,
        BigDecimal price,
        String status,
        String brandName,
        String categoryName,
        Integer availableQuantity,
        Integer reservedQuantity,
        Integer totalQuantity,
        Instant lastUpdated
) {
}
