package com.nexus.catalog.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nexus.messaging.bindings")
@Data
public class MessagingBindingProperties {
    private String publish;
}
