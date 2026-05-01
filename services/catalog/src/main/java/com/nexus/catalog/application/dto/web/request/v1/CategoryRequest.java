package com.nexus.catalog.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.InboundView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record CategoryRequest(

        @Null(groups = Validate.Create.class, message = "category.id.null")
        @NotNull(groups = Validate.Update.class, message = "category.id.required")
        @JsonView(InboundView.Update.class)
        Long id,

        @NotBlank(groups = Validate.Create.class, message = "category.name.required")
        @JsonView(InboundView.Common.class)
        String name,

        @NotBlank(groups = Validate.Create.class, message = "category.slug.required")
        @JsonView(InboundView.Common.class)
        String slug,

        @Min(value = 0, message = "category.parent.invalid")
        @JsonView(InboundView.Common.class)
        Long parentId,

        @JsonView(InboundView.Common.class)
        Boolean active

) {
}
