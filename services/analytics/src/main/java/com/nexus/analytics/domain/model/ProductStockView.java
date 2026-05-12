package com.nexus.analytics.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@NullMarked
@Table("product_stock_view")
@Getter
@Setter
@NoArgsConstructor
public class ProductStockView {

    @Id
    @Column("product_id")
    private Long productId;

    @Column("product_name")
    private String productName;

    private String slug;
    private String sku;
    private BigDecimal price;
    private String status;

    @Column("brand_name")
    private String brandName;

    @Column("category_name")
    private String categoryName;

    @Column("available_quantity")
    private Integer availableQuantity;

    @Column("reserved_quantity")
    private Integer reservedQuantity;

    @Column("total_quantity")
    private Integer totalQuantity;

    @Column("last_updated")
    private Instant lastUpdated;

}
