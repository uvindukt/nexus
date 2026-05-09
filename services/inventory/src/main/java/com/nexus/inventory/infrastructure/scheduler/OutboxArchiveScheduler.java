package com.nexus.inventory.infrastructure.scheduler;

import com.nexus.inventory.application.service.OutboxService;
import com.nexus.inventory.infrastructure.config.OutboxArchiveProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxArchiveScheduler {

    private final OutboxService outboxService;
    private final OutboxArchiveProperties properties;

    @Scheduled(cron = "${outbox.archive.cron}")
    public void run() {

        log.info("Outbox Archive Scheduler started");

        Instant threshold = Instant.now().minus(properties.getThresholdDays(), ChronoUnit.DAYS);
        int archived = outboxService.archive(threshold, properties.getBatchSize());

        log.info("Outbox Archive Scheduler finished - archived {} records", archived);

    }

}
