package com.nexus.inventory.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.inventory.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
public record StockResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        Long productId,

        @JsonView(OutboundView.Brief.class)
        Integer availableQuantity,

        @JsonView(OutboundView.Detailed.class)
        Integer reservedQuantity,

        @JsonView(OutboundView.Detailed.class)
        Instant updatedAt
) {
}
