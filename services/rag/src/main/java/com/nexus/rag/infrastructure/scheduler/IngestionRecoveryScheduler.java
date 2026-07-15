package com.nexus.rag.infrastructure.scheduler;

import com.nexus.rag.application.service.IngestionService;
import com.nexus.rag.application.service.ProductStockViewService;
import com.nexus.rag.domain.model.EmbeddingStatus;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.infrastructure.config.IngestionSchedulerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IngestionRecoveryScheduler {

    private final ProductStockViewService productStockViewService;
    private final IngestionSchedulerProperties properties;
    private final IngestionService ingestionService;

    @Scheduled(cron = "${ingestion.recovery-cron}")
    public void run() {

        log.debug("Starting ingestion recovery scheduler");

        List<ProductStockView> productStockViews = productStockViewService.claimProductBatchByEmbeddingStatus(EmbeddingStatus.FAILED, properties.getBatchSize());

        productStockViews.forEach(productStockView -> {

            if (productStockView.getRetries() < properties.getMaxRetries()) {
                ingestionService.upsertEmbedding(productStockView);
                productStockViewService.updateEmbeddingStatus(productStockView.getProductId(), EmbeddingStatus.EMBEDDED);
                log.info("Product embedded - Product ID: {}", productStockView.getProductId());
            } else {
                productStockViewService.markEmbeddingOpDead(productStockView.getProductId());
                log.error("Dead product embedding - Product ID: {}", productStockView.getProductId());
            }

        });

        log.debug("Ingestion recovery scheduler finished");

    }

}
