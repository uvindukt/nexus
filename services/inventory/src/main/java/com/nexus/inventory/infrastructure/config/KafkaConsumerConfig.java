package com.nexus.inventory.infrastructure.config;

import com.nexus.shared.InboxEnvelope;
import com.nexus.inventory.domain.port.in.InboxConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final InboxConsumer inboxConsumer;

    @Bean
    public Consumer<Message<InboxEnvelope>> consume() {

        return message -> {
            InboxEnvelope inboxEnvelope = message.getPayload();
            inboxConsumer.consumeInbox(inboxEnvelope);
        };

    }

}
