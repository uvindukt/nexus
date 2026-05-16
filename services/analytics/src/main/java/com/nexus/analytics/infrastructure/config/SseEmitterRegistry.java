package com.nexus.analytics.infrastructure.config;

import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterRegistry {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(clientId, emitter);

        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        emitter.onError(e -> emitters.remove(clientId));

        return emitter;
    }

    public void broadcast(SseEnvelope envelope) {
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(envelope.id())
                        .name(envelope.type())
                        .data(envelope.payload()));
            } catch (IOException e) {
                log.error("Failed to send SSE", e);
                emitters.remove(clientId);
            }
        });
    }

}
