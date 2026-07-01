package com.nexus.rag.infrastructure.messaging.consumer;

import com.nexus.rag.application.service.InboxService;
import com.nexus.rag.domain.port.in.StockConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaStockConsumer implements StockConsumer {

    private final InboxService inboxService;

    @Override
    public void consumeStock(InboxEnvelope inboxEnvelope) {
        inboxService.onStockEvent(inboxEnvelope);
    }

}
