package com.nexus.catalog.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.InboundView;
import com.nexus.catalog.domain.model.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(

        @Null(groups = Validate.Create.class, message = "product.id.null")
        @NotNull(groups = Validate.Update.class, message = "product.id.required")
        @JsonView(InboundView.Update.class)
        Long id,

        @NotBlank(groups = Validate.Create.class, message = "product.name.required")
        @JsonView(InboundView.Common.class)
        String name,

        @NotBlank(groups = Validate.Create.class, message = "product.slug.required")
        @JsonView(InboundView.Common.class)
        String slug,

        @JsonView(InboundView.Common.class)
        String description,

        @NotBlank(groups = Validate.Create.class, message = "product.sku.required")
        @JsonView(InboundView.Common.class)
        String sku,

        @NotNull(groups = Validate.Create.class, message = "product.price.required")
        @PositiveOrZero(groups = {Validate.Create.class, Validate.Update.class}, message = "product.price.positive")
        @JsonView(InboundView.Common.class)
        BigDecimal price,

        @Schema(implementation = ProductStatus.class)
        @NotNull(groups = Validate.Create.class, message = "product.status.required")
        @JsonView(InboundView.Common.class)
        ProductStatus status,

        @NotNull(groups = Validate.Create.class, message = "product.brand.required")
        @JsonView(InboundView.Common.class)
        Long brandId,

        @NotNull(groups = Validate.Create.class, message = "product.category.required")
        @JsonView(InboundView.Common.class)
        Long categoryId,

        @JsonView(InboundView.Update.class)
        List<ProductAttributeRequest> attributes

) {
}
