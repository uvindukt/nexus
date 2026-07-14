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
public class IngestionScheduler {

    private final ProductStockViewService productStockViewService;
    private final IngestionService ingestionService;
    private final IngestionSchedulerProperties properties;

    @Scheduled(cron = "${ingestion.cron}")
    public void run() {

        log.debug("Ingestion scheduler started");

        List<ProductStockView> productStockViews = productStockViewService.claimProductBatchByEmbeddingStatus(EmbeddingStatus.PENDING, properties.getBatchSize());
        productStockViews.forEach(productStockView -> {

            try {
                ingestionService.upsertEmbedding(productStockView);
                productStockViewService.updateEmbeddingStatus(productStockView.getProductId(), EmbeddingStatus.EMBEDDED);
                log.info("Product embedded - Product ID: {}", productStockView.getProductId());
            } catch (Exception e) {
                productStockViewService.markEmbeddingOpFailed(productStockView.getProductId());
                log.error("Ingestion scheduler failed - Product ID: {}", productStockView.getProductId(), e);
            }

        });

        log.debug("Ingestion scheduler finished");

    }

}
