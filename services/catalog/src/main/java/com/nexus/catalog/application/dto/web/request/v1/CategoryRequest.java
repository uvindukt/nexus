package com.nexus.catalog.application.dto.web.request.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.validation.Validate;
import com.nexus.catalog.application.dto.web.view.View;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record CategoryRequest(

        @Null(groups = Validate.Create.class, message = "{category.id.null}")
        @NotNull(groups = Validate.Update.class, message = "{category.id.required}")
        @JsonView({View.Update.class, View.Brief.class})
        Long id,

        @NotBlank(groups = Validate.Create.class, message = "{category.name.required}")
        @JsonView({View.Common.class, View.Brief.class})
        String name,

        @NotBlank(groups = Validate.Create.class, message = "{category.slug.required}")
        @JsonView({View.Common.class, View.Brief.class})
        String slug,

        @Min(value = 0, message = "{category.parent.invalid}")
        @JsonView({View.Common.class, View.Detail.class})
        Long parentId,

        @JsonView({View.Common.class, View.Detail.class})
        boolean isActive

) {
}
