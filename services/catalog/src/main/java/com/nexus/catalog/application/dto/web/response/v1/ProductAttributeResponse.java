package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ProductAttributeResponse(

        @JsonView(OutboundView.Brief.class)
        Long id,

        @JsonView(OutboundView.Brief.class)
        String key,

        @JsonView(OutboundView.Brief.class)
        String value

) {
}
