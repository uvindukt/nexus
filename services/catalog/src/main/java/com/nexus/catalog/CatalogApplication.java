package com.nexus.catalog;

import com.nexus.catalog.infrastructure.config.MessagingBindingProperties;
import com.nexus.catalog.infrastructure.config.OutboxArchiveProperties;
import com.nexus.catalog.infrastructure.config.OutboxProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.nexus.catalog"})
@EnableJpaRepositories(basePackages = {"com.nexus.catalog.infrastructure.persistence"})
@EnableDiscoveryClient
@EnableScheduling
@EnableConfigurationProperties({OutboxProperties.class, OutboxArchiveProperties.class, MessagingBindingProperties.class})
public class CatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

}
