package com.nexus.rag.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.rag.application.dto.web.OutboundView;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public record ChatResponse(
        @JsonView(OutboundView.Brief.class)
        UUID conversationId,
        @JsonView(OutboundView.Brief.class)
        String reply
) {
}
