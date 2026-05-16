package com.nexus.analytics.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;
import com.nexus.analytics.infrastructure.config.SseEmitterRegistry;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsRedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseEmitterRegistry emitterRegistry;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {

        try {
            SseEnvelope envelope = objectMapper.readValue(message.getBody(), SseEnvelope.class);
            emitterRegistry.broadcast(envelope); // Push to local SSE clients
        } catch (IOException e) {
            log.error("Failed to deserialize Redis message", e);
        }

    }

}
