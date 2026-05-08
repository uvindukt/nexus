package com.nexus.catalog.infrastructure.messaging.publisher;

import com.nexus.shared.OutboxEnvelope;
import com.nexus.catalog.domain.port.out.OutboxPublisher;
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
public class KafkaOutboxPublisher implements OutboxPublisher {

    private final StreamBridge streamBridge;
    private final MessagingBindingProperties messagingBindingProperties;

    @Override
    public void publishOutbox(OutboxEnvelope outboxEnvelope, String key) {

        Message<OutboxEnvelope> message = MessageBuilder
                .withPayload(outboxEnvelope)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        streamBridge.send(messagingBindingProperties.getPublish(), message);

    }

}
