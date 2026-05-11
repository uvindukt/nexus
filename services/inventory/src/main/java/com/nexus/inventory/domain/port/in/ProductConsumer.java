package com.nexus.inventory.domain.port.in;


import com.nexus.shared.common.InboxEnvelope;

public interface ProductConsumer {

    void consumeProduct(InboxEnvelope inboxEnvelope);

}
