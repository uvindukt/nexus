package com.nexus.analytics.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis")
@Data
public class RedisProperties {
    String channel;
}
