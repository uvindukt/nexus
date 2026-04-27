package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.view.OutboundView;
import com.nexus.catalog.domain.model.ProductStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NullMarked
public record ProductResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        String sku,

        @JsonView(OutboundView.Brief.class)
        String name,

        @JsonView(OutboundView.Brief.class)
        String slug,

        @Nullable
        @JsonView(OutboundView.Detail.class)
        String description,

        @JsonView(OutboundView.Brief.class)
        BigDecimal price,

        @JsonView(OutboundView.Brief.class)
        ProductStatus status,

        @Nullable
        @JsonView(OutboundView.Detail.class)
        BrandResponse brand,

        @Nullable
        @JsonView(OutboundView.Detail.class)
        CategoryResponse category,

        @JsonView(OutboundView.Detail.class)
        List<ProductAttributeResponse> attributes,

        @JsonView(OutboundView.Detail.class)
        Instant createdAt,

        @JsonView(OutboundView.Detail.class)
        Instant updatedAt

) {
}
