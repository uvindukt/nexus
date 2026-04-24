package com.nexus.catalog.domain.model;

import com.nexus.catalog.domain.exception.InvalidProductStateException;
import com.nexus.catalog.domain.exception.ProductErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
    private String code;

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
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductAttribute> attributes = new HashSet<>();

    @Version
    private Integer version;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * Sets the product status to active.
     *
     * @throws InvalidProductStateException if the product is already deleted.
     */
    public void activate() {

        if (this.status == ProductStatus.DELETED) {
            throw new InvalidProductStateException(ProductErrorCode.ACTIVATION_OF_DELETED_PRODUCT);
        }
        this.status = ProductStatus.ACTIVE;

    }

    /**
     * Adds custom attributes to a {@link Product} if needed
     *
     * @param key   Custom attribute key (eg: color)
     * @param value Custom attribute value (eg: black)
     */
    public void addAttribute(String key, String value) {

        ProductAttribute attribute = ProductAttribute.builder()
                .key(key)
                .value(value)
                .product(this)
                .build();
        this.attributes.add(attribute);

    }

}