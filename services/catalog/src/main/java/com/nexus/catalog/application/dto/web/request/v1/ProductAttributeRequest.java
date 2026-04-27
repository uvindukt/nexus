package com.nexus.catalog.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.validation.Validate;
import com.nexus.catalog.application.dto.web.view.InboundView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record ProductAttributeRequest(

        @Null(groups = Validate.Create.class, message = "product.attribute.id.null")
        @NotNull(groups = Validate.Update.class, message = "product.attribute.id.required")
        @JsonView(InboundView.Update.class)
        Long id,

        @NotBlank(groups = Validate.Create.class, message = "product.attribute.key.required")
        @JsonView(InboundView.Common.class)
        String key,

        @NotBlank(groups = Validate.Create.class, message = "product.attribute.value.required")
        @JsonView(InboundView.Common.class)
        String value,

        @Min(value = 0, message = "product.attribute.product.invalid")
        @NotNull(groups = Validate.Create.class, message = "product.attribute.product.required")
        @JsonView(InboundView.Common.class)
        Long productId

) {
}
