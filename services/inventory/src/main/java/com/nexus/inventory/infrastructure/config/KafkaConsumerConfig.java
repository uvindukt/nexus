package com.nexus.inventory.infrastructure.config;

import com.nexus.inventory.domain.port.in.ProductConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ProductConsumer productConsumer;

    @Bean
    public Consumer<Message<InboxEnvelope>> consumeProduct() {

        return message -> {

            InboxEnvelope inboxEnvelope = message.getPayload();
            Acknowledgment ack = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

            try {
                productConsumer.consumeProduct(inboxEnvelope);
                if (ack != null) {
                    ack.acknowledge();
                }
            } catch (Exception e) {
                log.error("Failed to process product event - ID: {}", inboxEnvelope.id(), e);
                // don't ack — Spring retry + DLQ will handle it
                throw e;
            }

        };

    }

}
