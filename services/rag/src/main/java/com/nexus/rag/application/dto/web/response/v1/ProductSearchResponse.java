package com.nexus.rag.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.rag.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public record ProductSearchResponse(
        @JsonView(OutboundView.Brief.class)
        String answer,
        @Nullable
        @JsonView(OutboundView.Brief.class)
        List<ProductStockViewResponse> products
) {
}
