package com.nexus.catalog.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "product_attribute",
        indexes = {
                @Index(name = "idx_product_attribute_product_id", columnList = "product_id"),
                @Index(name = "idx_product_attribute_key", columnList = "key")
        }
)
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attribute_seq")
    @SequenceGenerator(name = "attribute_seq", sequenceName = "attribute_seq", allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

}