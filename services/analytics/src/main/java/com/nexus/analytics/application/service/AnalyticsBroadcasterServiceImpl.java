package com.nexus.analytics.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.application.dto.web.event.v1.ProductStockViewEvent;
import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsBroadcasterServiceImpl implements AnalyticsBroadcasterService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${redis.channel}")
    private String redisChannel;

    @Transactional
    @Override
    public void publish(ProductStockViewEvent event) {
        SseEnvelope envelope = event.envelope();
        try {
            String envelopeValue = objectMapper.writeValueAsString(envelope);
            redisTemplate.convertAndSend(redisChannel, envelopeValue);
            log.info("Published event to Redis - ID: {}", envelope.id());
        } catch (JsonProcessingException e) {
            log.error("Failed to publish event to Redis - ID: {}", envelope.id(), e);
        }

    }

}
