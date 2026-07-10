package com.nexus.catalog.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@NullMarked
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "category",
        indexes = {
                @Index(name = "idx_category_parent_id", columnList = "parent_id")
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Category> subCategories = new HashSet<>();

    @Builder.Default
    private Boolean active = true;

}