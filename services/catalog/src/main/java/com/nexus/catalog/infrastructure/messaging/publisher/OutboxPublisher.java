package com.nexus.catalog.infrastructure.messaging.publisher;

import com.nexus.catalog.application.dto.messaging.publisher.v1.OutboxEvent;
import com.nexus.catalog.domain.port.out.OutboxPublisherPort;
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
public class OutboxPublisher implements OutboxPublisherPort {

    private final StreamBridge streamBridge;
    private final MessagingBindingProperties messagingBindingProperties;

    public void publishOutbox(OutboxEvent outboxEvent, String key) {

        Message<OutboxEvent> message = MessageBuilder
                .withPayload(outboxEvent)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        streamBridge.send(messagingBindingProperties.getProductOut(), message);

    }

}
