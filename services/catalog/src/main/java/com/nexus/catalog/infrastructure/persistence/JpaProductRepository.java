package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.repository.ProductRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long>, ProductRepository {

    @Override
    List<Product> findBySkuOrSlug(String sku, String slug);

    @Override
    List<Product> findBySkuInOrSlugIn(Collection<String> skus, Collection<String> slugs);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    List<Product> findAll();

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    Optional<Product> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    Optional<Product> findBySku(String sku);

}
