package com.nexus.analytics.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsBroadcasterServiceImpl implements AnalyticsBroadcasterService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void publish(SseEnvelope envelope) {

        try {
            String envelopeValue = objectMapper.writeValueAsString(envelope);
            redisTemplate.convertAndSend("${redis.channel}", envelopeValue);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish event to Redis - ID: {}", envelope.id(), e);
        }

    }

}
