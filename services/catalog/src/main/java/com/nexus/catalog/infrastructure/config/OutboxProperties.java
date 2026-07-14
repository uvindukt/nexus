package com.nexus.catalog.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "outbox")
@Data
public class OutboxProperties {
    String cron;
    Integer batchSize;
    Integer maxRetries;
}
