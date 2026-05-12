package com.nexus.analytics.infrastructure.messaging.consumer;

import com.nexus.analytics.application.service.InboxService;
import com.nexus.analytics.domain.port.in.ProductConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaProductConsumer implements ProductConsumer {

    private final InboxService inboxService;

    @Override
    public Mono<Void> consumeProduct(InboxEnvelope inboxEnvelope) {
        return inboxService.onProductEvent(inboxEnvelope);
    }

}
