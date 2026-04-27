package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.ProductAttribute;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"product"})
    Optional<ProductAttribute> findById(Long Id);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"product"})
    List<ProductAttribute> findAll();

    @EntityGraph(attributePaths = {"product"})
    Optional<ProductAttribute> findByKey(String key);

    @EntityGraph(attributePaths = {"product"})
    List<ProductAttribute> findAllByProduct_Id(Long productId);

}
