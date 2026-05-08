package com.nexus.inventory.domain.port.in;


import com.nexus.shared.InboxEnvelope;

public interface InboxConsumer {

    void consumeInbox(InboxEnvelope inboxEnvelope);

}
