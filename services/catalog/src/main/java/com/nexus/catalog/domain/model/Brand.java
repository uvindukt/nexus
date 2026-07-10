package com.nexus.catalog.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@NullMarked
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_seq")
    @SequenceGenerator(name = "brand_seq", sequenceName = "brand_seq", allocationSize = 20)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Nullable
    @Column(unique = true)
    private String website;

    @Nullable
    private String logoUrl;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<Product> products = new LinkedHashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

}
