package com.nexus.catalog.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "outbox")
@Data
public class OutboxProperties {
    String cron;
    Integer thresholdDays;
}
