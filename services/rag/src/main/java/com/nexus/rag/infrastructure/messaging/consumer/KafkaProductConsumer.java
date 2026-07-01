package com.nexus.rag.infrastructure.messaging.consumer;

import com.nexus.rag.application.service.InboxService;
import com.nexus.rag.domain.port.in.ProductConsumer;
import com.nexus.shared.common.InboxEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProductConsumer implements ProductConsumer {

    private final InboxService inboxService;

    @Override
    public void consumeProduct(InboxEnvelope inboxEnvelope) {
        inboxService.onProductEvent(inboxEnvelope);
    }

}
