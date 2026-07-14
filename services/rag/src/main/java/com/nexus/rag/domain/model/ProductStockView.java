package com.nexus.rag.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockView {

    @Id
    private Long productId;
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmbeddingStatus embeddingStatus = EmbeddingStatus.PENDING;
    private String slug;
    private String sku;
    private BigDecimal price;
    private String status;
    private String brandName;
    private String categoryName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer totalQuantity;

    @LastModifiedDate
    private Instant lastUpdated;

}