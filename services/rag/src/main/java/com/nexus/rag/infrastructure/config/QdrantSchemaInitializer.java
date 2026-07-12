package com.nexus.rag.infrastructure.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QdrantSchemaInitializer implements ApplicationRunner {

    private static final String PRODUCT_ID_FIELD = "productId";

    private final VectorStore vectorStore;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        QdrantClient client = vectorStore.getNativeClient()
                .map(QdrantClient.class::cast)
                .orElseThrow(() -> new IllegalStateException("Native Qdrant client not available"));

        Collections.CollectionInfo info = client.getCollectionInfoAsync(collectionName).get();

        if (!info.getPayloadSchemaMap().containsKey(PRODUCT_ID_FIELD)) {
            client.createPayloadIndexAsync(collectionName, PRODUCT_ID_FIELD, Collections.PayloadSchemaType.Keyword, null, true, null, null).get();
        }

    }

}