package com.nexus.catalog.infrastructure.scheduler;

import com.nexus.catalog.application.service.OutboxService;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxStatus;
import com.nexus.catalog.domain.repository.OutboxRepository;
import com.nexus.catalog.infrastructure.config.OutboxProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

        List<Outbox> outboxRecords = outboxRepository.findByStatusAndRetryCountLessThan(OutboxStatus.PENDING, properties.getMaxRetries(), PageRequest.of(0, properties.getBatchSize()));

        if (!outboxRecords.isEmpty()) {
            log.info("Outbox scheduler started");
        }

        int failures = 0;
        for (Outbox outbox : outboxRecords) {

            try {
                outboxService.publishSingle(outbox);
            } catch (Exception e) {

                if (outbox.getRetryCount() + 1 >= properties.getMaxRetries()) {
                    outboxService.markFailedEvent(outbox);
                } else {
                    outboxService.markFailedAttempt(outbox);
                }

                failures++;
                log.error("Failed to publish outbox record {}", outbox.getId(), e);
            }

        }

        if (!outboxRecords.isEmpty()) {
            log.info("Outbox scheduler finished. Published {}/{} records ({} failed)", outboxRecords.size() - failures, outboxRecords.size(), failures);
        }


    }

}
