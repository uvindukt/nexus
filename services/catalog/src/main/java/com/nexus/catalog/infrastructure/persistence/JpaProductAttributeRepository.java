package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.ProductAttribute;
import com.nexus.catalog.domain.repository.ProductAttributeRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductAttributeRepository extends JpaRepository<ProductAttribute, Long>, ProductAttributeRepository {

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"product"})
    Optional<ProductAttribute> findById(Long Id);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"product"})
    List<ProductAttribute> findAll();

    @Override
    @EntityGraph(attributePaths = {"product"})
    Optional<ProductAttribute> findByKey(String key);

    @Override
    @EntityGraph(attributePaths = {"product"})
    List<ProductAttribute> findAllByProduct_Id(Long productId);

}
