package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.ProductAttribute;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for ProductAttribute persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface ProductAttributeRepository extends ListCrudRepository<ProductAttribute, Long> {

    Optional<ProductAttribute> findByKeyAndProduct_Id(String key, Long productId);

    List<ProductAttribute> findAllByProduct_Id(Long productId);

}
