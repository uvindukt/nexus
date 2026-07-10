package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Brand;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Port interface for Brand persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface BrandRepository extends ListCrudRepository<Brand, Long> {

    Optional<Brand> findByName(String name);

    List<Brand> findByNameIn(Collection<String> names);

}
