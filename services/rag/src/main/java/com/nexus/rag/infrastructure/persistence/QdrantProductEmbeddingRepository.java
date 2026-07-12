package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.repository.ProductEmbeddingRepository;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.RetrievedPoint;
import io.qdrant.client.grpc.Points.ScrollPoints;
import io.qdrant.client.grpc.Points.ScrollResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.nexus.rag.infrastructure.persistence.constant.RepositoryConstants.QDRANT_PRODUCT_EMBEDDING_REPOSITORY;
import static io.qdrant.client.ConditionFactory.matchKeyword;
import static io.qdrant.client.WithPayloadSelectorFactory.include;

@Repository(QDRANT_PRODUCT_EMBEDDING_REPOSITORY)
@RequiredArgsConstructor
public class QdrantProductEmbeddingRepository implements ProductEmbeddingRepository {

    private static final String PAYLOAD_PRODUCT_ID_KEY = "productId";
    private static final String PAYLOAD_CONTENT_HASH_KEY = "contentHash";

    private final VectorStore vectorStore;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;


    @NullMarked
    @Override
    public Optional<String> findContentHashByProductId(Long productId) {

        QdrantClient qdrantClient = vectorStore.getNativeClient()
                .map(QdrantClient.class::cast)
                .orElseThrow(() -> new IllegalStateException("Native Qdrant client not available for VectorStore"));

        ScrollPoints request = ScrollPoints.newBuilder()
                .setCollectionName(collectionName)
                .setFilter(io.qdrant.client.grpc.Common.Filter.newBuilder()
                        .addMust(matchKeyword(PAYLOAD_PRODUCT_ID_KEY, productId.toString()))
                        .build())
                .setLimit(1)
                .setWithPayload(include(List.of(PAYLOAD_CONTENT_HASH_KEY)))
                .build();

        try {

            ScrollResponse response = qdrantClient.scrollAsync(request).get();
            return response.getResultList().stream()
                    .findFirst()
                    .map(RetrievedPoint::getPayloadMap)
                    .map(payload -> payload.get(PAYLOAD_CONTENT_HASH_KEY))
                    .map(io.qdrant.client.grpc.JsonWithInt.Value::getStringValue);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while querying Qdrant for productId=" + productId, e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Failed to query Qdrant for productId=" + productId, e.getCause());
        }

    }

}