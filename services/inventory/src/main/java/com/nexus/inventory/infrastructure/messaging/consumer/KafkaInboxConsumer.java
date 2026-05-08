package com.nexus.inventory.infrastructure.messaging.consumer;

import com.nexus.inventory.application.service.InboxService;
import com.nexus.shared.InboxEnvelope;
import com.nexus.inventory.domain.port.in.InboxConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaInboxConsumer implements InboxConsumer {

    private final InboxService inboxService;

    @Override
    public void consumeInbox(InboxEnvelope inboxEnvelope) {
        inboxService.onStockEvent(inboxEnvelope);
    }

}
