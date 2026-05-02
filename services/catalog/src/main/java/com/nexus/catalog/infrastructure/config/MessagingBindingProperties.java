package com.nexus.catalog.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "nexus.messaging.bindings")
@Data
public class MessagingBindingProperties {
    private String productOut;
}
