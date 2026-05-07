package com.nexus.inventory.infrastructure.scheduler;

import com.nexus.inventory.application.service.InboxService;
import com.nexus.inventory.infrastructure.config.InboxArchiveProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class InboxArchiveScheduler {

    private final InboxService inboxService;
    private final InboxArchiveProperties properties;

    @Scheduled(cron = "${inbox.archive.cron}")
    public void run() {

        log.info("Inbox Archive Scheduler started");

        Instant threshold = Instant.now().minus(properties.getThresholdDays(), ChronoUnit.DAYS);
        int archived = inboxService.archive(properties.getBatchSize());

        log.info("Inbox Archive Scheduler finished - archived {} records", archived);

    }

}
