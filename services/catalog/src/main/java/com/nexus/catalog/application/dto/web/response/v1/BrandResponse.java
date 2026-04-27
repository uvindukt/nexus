package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.view.OutboundView;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
public record BrandResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        String name,

        @JsonView(OutboundView.Detail.class)
        String website,

        @JsonView(OutboundView.Detail.class)
        String logoUrl,

        @JsonView(OutboundView.Detail.class)
        Boolean active,

        @JsonView(OutboundView.Detail.class)
        Instant createdDate

) {
}
