package com.nexus.rag.application.dto.web.request.v1;

import java.util.UUID;

public record ChatRequest(
        UUID conversationId,
        String message
) {
}
