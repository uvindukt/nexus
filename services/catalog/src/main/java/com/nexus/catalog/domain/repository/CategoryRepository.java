package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for Category persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    List<Category> findByNameOrSlug(String name, String slug);

    List<Category> findAll();

}
