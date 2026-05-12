package com.nexus.analytics.infrastructure.config;

import com.nexus.analytics.domain.port.in.ProductConsumer;
import com.nexus.analytics.domain.port.in.StockConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ProductConsumer productConsumer;
    private final StockConsumer stockConsumer;

    @Bean
    public Function<Message<InboxEnvelope>, Mono<Void>> consumeProduct() {
        return message -> productConsumer.consumeProduct(message.getPayload());
    }

    @Bean
    public Function<Message<InboxEnvelope>, Mono<Void>> consumeStock() {
        return message -> stockConsumer.consumeStock(message.getPayload());
    }


}
