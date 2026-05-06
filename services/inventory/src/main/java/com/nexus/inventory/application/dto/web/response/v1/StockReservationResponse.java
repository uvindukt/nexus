package com.nexus.inventory.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.inventory.application.dto.web.OutboundView;
import com.nexus.inventory.domain.model.StockReservationStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record StockReservationResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        Long orderId,

        @JsonView(OutboundView.Brief.class)
        Long productId,

        @JsonView(OutboundView.Brief.class)
        Integer quantity,

        @JsonView(OutboundView.Detailed.class)
        StockReservationStatus status
) {
}
