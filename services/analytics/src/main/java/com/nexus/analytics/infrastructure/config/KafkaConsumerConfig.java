package com.nexus.analytics.infrastructure.config;

import com.nexus.analytics.domain.port.in.ProductConsumer;
import com.nexus.analytics.domain.port.in.StockConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ProductConsumer productConsumer;
    private final StockConsumer stockConsumer;

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeProduct() {
        return message -> {
            log.info("Received product message: {}", message.getPayload());
            productConsumer.consumeProduct(message.getPayload())
                    .doOnError(err -> log.error("Error processing product message", err))
                    .subscribe();
        };
    }

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeStock() {
        return message -> {
            log.info("Received stock message: {}", message.getPayload());
            stockConsumer.consumeStock(message.getPayload())
                    .doOnError(err -> log.error("Error processing stock message", err))
                    .subscribe();
        };
    }


}
