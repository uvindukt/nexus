package com.nexus.analytics.infrastructure.config;

import com.nexus.analytics.domain.port.in.ProductConsumer;
import com.nexus.analytics.domain.port.in.StockConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ProductConsumer productConsumer;
    private final StockConsumer stockConsumer;

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeProduct() {

        return message -> {
            InboxEnvelope inboxEnvelope = message.getPayload();
            productConsumer.consumeProduct(inboxEnvelope);
        };

    }

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeStock() {

        return message -> {
            InboxEnvelope inboxEnvelope = message.getPayload();
            stockConsumer.consumeStock(inboxEnvelope);
        };

    }

}
