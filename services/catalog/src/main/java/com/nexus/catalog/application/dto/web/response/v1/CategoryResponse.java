package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.view.View;

import java.util.List;

public record CategoryResponse(

        @JsonView(View.Brief.class)
        Long id,

        @JsonView(View.Brief.class)
        String name,

        @JsonView(View.Brief.class)
        String slug,

        @JsonView(View.Detail.class)
        CategoryResponse parent,

        @JsonView(View.Detail.class)
        List<CategoryResponse> subCategories,

        @JsonView(View.Detail.class)
        boolean isActive

) {
}
