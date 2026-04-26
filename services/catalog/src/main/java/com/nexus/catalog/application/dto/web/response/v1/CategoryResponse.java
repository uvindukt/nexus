package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.view.OutboundView;

import java.util.List;

public record CategoryResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        String name,

        @JsonView(OutboundView.Brief.class)
        String slug,

        @JsonView(OutboundView.Detail.class)
        CategoryResponse parent,

        @JsonView(OutboundView.Detail.class)
        List<CategoryResponse> subCategories,

        @JsonView(OutboundView.Detail.class)
        boolean isActive

) {
}
