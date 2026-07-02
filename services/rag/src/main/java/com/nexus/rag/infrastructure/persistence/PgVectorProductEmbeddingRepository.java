package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.repository.ProductEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PgVectorProductEmbeddingRepository implements ProductEmbeddingRepository {

    private static final String NATIVE_QUERY = """
            SELECT metadata ->> 'contentHash'
            FROM vector_store
            WHERE metadata ->> 'productId' = ?
            """;

    private final VectorStore vectorStore;

    @NullMarked
    @Override
    public Optional<String> findContentHashByProductId(Long productId) {

        JdbcTemplate jdbcTemplate = vectorStore.getNativeClient()
                .map(JdbcTemplate.class::cast)
                .orElseThrow(() -> new IllegalStateException("Native JDBC client not available for VectorStore"));

        return jdbcTemplate.query(NATIVE_QUERY, (rs, rowNum) -> rs.getString(1), productId.toString()).stream().findFirst();

    }

}