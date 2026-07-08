package com.nexus.rag.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.rag.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.time.Instant;

@NullMarked
public record ProductStockViewResponse(
        @JsonView(OutboundView.Brief.class)
        Long productId,
        @JsonView(OutboundView.Brief.class)
        String productName,
        @JsonView(OutboundView.Brief.class)
        String slug,
        @JsonView(OutboundView.Brief.class)
        String sku,
        @JsonView(OutboundView.Brief.class)
        BigDecimal price,
        @JsonView(OutboundView.Brief.class)
        String status,
        @JsonView(OutboundView.Brief.class)
        String brandName,
        @JsonView(OutboundView.Brief.class)
        String categoryName,
        @JsonView(OutboundView.Detail.class)
        String description,
        @JsonView(OutboundView.Brief.class)
        Integer availableQuantity,
        @JsonView(OutboundView.Detail.class)
        Integer reservedQuantity,
        @JsonView(OutboundView.Detail.class)
        Integer totalQuantity,
        @JsonView(OutboundView.Detail.class)
        Instant lastUpdated
) {
}
