package com.nexus.catalog.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.validation.Validate;
import com.nexus.catalog.application.dto.web.view.InboundView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record BrandRequest(

        @Null(groups = Validate.Create.class, message = "brand.id.null")
        @NotNull(groups = Validate.Update.class, message = "brand.id.required")
        @JsonView(InboundView.Update.class)
        Long id,

        @NotBlank(groups = Validate.Create.class, message = "brand.name.required")
        @JsonView(InboundView.Common.class)
        String name,

        @JsonView(InboundView.Common.class)
        String website,

        @JsonView(InboundView.Common.class)
        String logoUrl,

        @JsonView(InboundView.Common.class)
        Boolean active

) {
}
