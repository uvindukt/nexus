package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Product;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    List<Product> findBySkuOrSlug(String sku, String slug);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    List<Product> findAll();

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"brand", "category", "attributes"})
    Optional<Product> findBySku(String sku);

}
