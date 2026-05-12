package com.nexus.analytics.domain.port.in;

import com.nexus.shared.common.InboxEnvelope;
import reactor.core.publisher.Mono;

public interface ProductConsumer {
    Mono<Void> consumeProduct(InboxEnvelope inboxEnvelope);
}
