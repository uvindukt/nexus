package com.nexus.catalog.infrastructure.messaging.publisher;

import com.nexus.catalog.application.dto.messaging.ProductCreatedEvent;
import com.nexus.catalog.application.dto.messaging.envelope.EventEnvelope;
import com.nexus.catalog.infrastructure.config.MessagingBindingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final StreamBridge streamBridge;
    private final MessagingBindingProperties messagingBindingProperties;

    public void publishProductCreated(EventEnvelope<ProductCreatedEvent> eventEnvelope, String key) {

        Message<EventEnvelope<ProductCreatedEvent>> message = MessageBuilder
                .withPayload(eventEnvelope)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        streamBridge.send(messagingBindingProperties.getProductOut(), message);
    }

}
