package com.nexus.inventory.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.inventory.application.dto.web.InboundView;
import com.nexus.inventory.application.dto.web.Validate;
import jakarta.validation.constraints.NotNull;

public record StockRequest(

        @JsonView(InboundView.Common.class)
        @NotNull(groups = Validate.Update.class, message = "stock.productId.required")
        Long productId,

        @JsonView(InboundView.Common.class)
        @NotNull(groups = Validate.Update.class, message = "stock.quantity.required")
        Integer quantity

) {
}
