package com.nexus.inventory.domain.port.in;


import com.nexus.shared.inbox.InboxEnvelope;

public interface ProductConsumer {

    void consumeProduct(InboxEnvelope inboxEnvelope);

}
