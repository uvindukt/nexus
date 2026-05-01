package com.nexus.catalog.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "outbox.archive")
@Data
public class OutboxArchiveProperties {
    String cron;
    Integer thresholdDays;
    Integer batchSize;
}
