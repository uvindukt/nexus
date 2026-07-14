package com.nexus.rag.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ingestion")
@Data
public class IngestionSchedulerProperties {
    String cron;
    Integer batchSize;
    Integer maxRetries;
}
