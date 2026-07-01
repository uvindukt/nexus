package com.nexus.rag.domain.port.in;

import com.nexus.shared.common.InboxEnvelope;

public interface StockConsumer {
    void consumeStock(InboxEnvelope inboxEnvelope);
}
