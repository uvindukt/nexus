package com.nexus.analytics.infrastructure.messaging.consumer;

import com.nexus.analytics.application.service.InboxService;
import com.nexus.analytics.domain.port.in.StockConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaStockConsumer implements StockConsumer {

    private final InboxService inboxService;

    @Override
    public Mono<Void> consumeStock(InboxEnvelope inboxEnvelope) {
        return inboxService.onStockEvent(inboxEnvelope);
    }

}
