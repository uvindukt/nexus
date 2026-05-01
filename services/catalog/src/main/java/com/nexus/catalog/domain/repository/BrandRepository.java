package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Brand;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for Brand persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface BrandRepository {

    Brand save(Brand brand);

    Optional<Brand> findById(Long id);

    Optional<Brand> findByName(String name);

    List<Brand> findAll();

}
