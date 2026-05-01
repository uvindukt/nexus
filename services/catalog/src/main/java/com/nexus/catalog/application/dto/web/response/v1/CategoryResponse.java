package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public record CategoryResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        String name,

        @JsonView(OutboundView.Brief.class)
        String slug,

        @Nullable
        @JsonView(OutboundView.Detail.class)
        CategoryResponse parent,

        @JsonView(OutboundView.Detail.class)
        List<CategoryResponse> subCategories,

        @JsonView(OutboundView.Detail.class)
        boolean active

) {
}
