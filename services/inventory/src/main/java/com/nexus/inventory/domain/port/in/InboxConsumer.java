package com.nexus.inventory.domain.port.in;


import com.nexus.inventory.domain.model.InboxEnvelope;

public interface InboxConsumer {

    void consumeInbox(InboxEnvelope inboxEnvelope);

}
