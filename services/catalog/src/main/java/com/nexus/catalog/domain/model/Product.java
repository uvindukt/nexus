package com.nexus.catalog.domain.model;

import com.nexus.catalog.domain.exception.InvalidProductStateException;
import com.nexus.catalog.domain.exception.ProductErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NullMarked
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(0)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.DRAFT;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductAttribute> attributes = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Nullable
    @UpdateTimestamp
    private Instant updatedAt;

    // ── Domain Behaviour ──────────────────────────────────────────

    /**
     * Soft-deletes this product.
     *
     * @throws InvalidProductStateException if already deleted
     */
    public void markAsDeleted() {
        if (this.status == ProductStatus.DELETED) {
            throw new InvalidProductStateException(ProductErrorCode.ACTIVATION_OF_DELETED_PRODUCT);
        }
        this.status = ProductStatus.DELETED;
    }

    /**
     * Activates a draft or archived product.
     *
     * @throws InvalidProductStateException if the product is deleted
     */
    public void activate() {
        if (this.status == ProductStatus.DELETED) {
            throw new InvalidProductStateException(ProductErrorCode.ACTIVATION_OF_DELETED_PRODUCT);
        }
        this.status = ProductStatus.ACTIVE;
    }

}