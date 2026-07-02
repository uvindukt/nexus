package com.nexus.rag.domain.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ProductEmbeddingRepository {

    Optional<String> findContentHashByProductId(Long productId);

}
