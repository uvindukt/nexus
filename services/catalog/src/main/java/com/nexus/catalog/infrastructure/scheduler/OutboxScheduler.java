package com.nexus.catalog.infrastructure.scheduler;

import com.nexus.catalog.application.service.OutboxService;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxStatus;
import com.nexus.catalog.domain.repository.OutboxRepository;
import com.nexus.catalog.infrastructure.config.OutboxProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxService outboxService;
    private final OutboxProperties properties;
    private final OutboxRepository outboxRepository;

    @Scheduled(cron = "${outbox.cron}")
    public void run() {

        log.info("Outbox scheduler started");

        List<Outbox> outboxRecords = outboxRepository.findByStatus(OutboxStatus.PROCESSED);

        outboxRecords.forEach(outbox -> {
            try {
                outboxService.publishSingle(outbox);
            } catch (Exception e) {
                log.error("Failed to publish outbox record {}", outbox.getId(), e);
            }
        });

    }

}
