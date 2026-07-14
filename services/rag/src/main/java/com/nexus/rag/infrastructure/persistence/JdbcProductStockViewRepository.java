package com.nexus.rag.infrastructure.persistence;

import com.nexus.rag.domain.model.EmbeddingStatus;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdbcProductStockViewRepository extends JpaRepository<ProductStockView, Long>, ProductStockViewRepository {

    List<ProductStockView> findByProductIdIn(List<Long> productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")) // "-2" is for SKIP LOCKED, so a parallel poller would skip not wait
    List<ProductStockView> findByEmbeddingStatus(EmbeddingStatus embeddingStatus, Pageable pageable);

}
