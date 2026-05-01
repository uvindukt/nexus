package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.ProductAttribute;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for ProductAttribute persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface ProductAttributeRepository extends ListCrudRepository<ProductAttribute, Long> {

    Optional<ProductAttribute> findByKey(String key);

    List<ProductAttribute> findAllByProduct_Id(Long productId);

}
