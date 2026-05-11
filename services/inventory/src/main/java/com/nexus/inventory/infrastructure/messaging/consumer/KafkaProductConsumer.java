package com.nexus.inventory.infrastructure.messaging.consumer;

import com.nexus.inventory.application.service.InboxService;
import com.nexus.shared.common.InboxEnvelope;
import com.nexus.inventory.domain.port.in.ProductConsumer;
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
