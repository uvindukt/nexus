package com.nexus.catalog.infrastructure.messaging.publisher;

import com.nexus.catalog.domain.port.out.ProductPublisher;
import com.nexus.catalog.infrastructure.config.MessagingBindingProperties;
import com.nexus.shared.common.OutboxEnvelope;
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
public class KafkaProductPublisher implements ProductPublisher {

    private final StreamBridge streamBridge;
    private final MessagingBindingProperties messagingBindingProperties;

    @Override
    public void publishProduct(OutboxEnvelope outboxEnvelope, String key) {

        Message<OutboxEnvelope> message = MessageBuilder
                .withPayload(outboxEnvelope)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        boolean isSent = streamBridge.send(messagingBindingProperties.getPublish(), message);

        if (!isSent) {
            log.warn("Failed to send message ID - {}", outboxEnvelope.id());
        }

    }

}
