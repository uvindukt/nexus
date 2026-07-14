package com.nexus.rag;

import com.nexus.rag.infrastructure.config.IngestionSchedulerProperties;
import org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {OllamaChatAutoConfiguration.class})
@EnableScheduling
@EnableConfigurationProperties({IngestionSchedulerProperties.class})
public class RagApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }

}
