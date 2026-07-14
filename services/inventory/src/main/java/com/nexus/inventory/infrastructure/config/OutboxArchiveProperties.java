package com.nexus.inventory.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "outbox.archive")
@Data
public class OutboxArchiveProperties {
    String cron;
    Integer thresholdDays;
    Integer batchSize;
}
