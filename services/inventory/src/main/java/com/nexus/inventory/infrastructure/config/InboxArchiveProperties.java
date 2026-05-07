package com.nexus.inventory.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inbox.archive")
@Data
public class InboxArchiveProperties {
    String cron;
    Integer thresholdDays;
    Integer batchSize;
}
