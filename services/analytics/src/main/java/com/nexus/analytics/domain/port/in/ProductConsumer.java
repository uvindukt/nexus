package com.nexus.analytics.domain.port.in;

import com.nexus.shared.common.InboxEnvelope;

public interface ProductConsumer {
    void consumeProduct(InboxEnvelope inboxEnvelope);
}
