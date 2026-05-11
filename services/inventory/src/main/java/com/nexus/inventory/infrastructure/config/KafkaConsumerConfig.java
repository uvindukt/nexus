package com.nexus.inventory.infrastructure.config;

import com.nexus.shared.common.InboxEnvelope;
import com.nexus.inventory.domain.port.in.ProductConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ProductConsumer productConsumer;

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeProduct() {

        return message -> {
            InboxEnvelope inboxEnvelope = message.getPayload();
            productConsumer.consumeProduct(inboxEnvelope);
        };

    }

}
