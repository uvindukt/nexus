package com.nexus.analytics.domain.port.in;

import com.nexus.shared.common.InboxEnvelope;
import reactor.core.publisher.Mono;

public interface StockConsumer {
    Mono<Void> consumeStock(InboxEnvelope inboxEnvelope);
}
