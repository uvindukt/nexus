package com.nexus.analytics.domain.port.in;

import com.nexus.shared.common.InboxEnvelope;

public interface StockConsumer {
    void consumeStock(InboxEnvelope inboxEnvelope);
}
