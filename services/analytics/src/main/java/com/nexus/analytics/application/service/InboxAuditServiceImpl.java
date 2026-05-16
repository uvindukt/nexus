package com.nexus.analytics.application.service;

import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.repository.InboxRepository;
import com.nexus.shared.common.InboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxAuditServiceImpl implements InboxAuditService {

    private final InboxRepository inboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveAsFailed(Inbox inbox) {

        Inbox failed = Inbox.builder()
                .id(inbox.getId())
                .type(inbox.getType())
                .aggregateType(inbox.getAggregateType())
                .aggregateId(inbox.getAggregateId())
                .payload(inbox.getPayload())
                .status(InboxStatus.FAILED)
                .processedAt(Instant.now())
                .build();
        inboxRepository.save(failed);

    }

}
