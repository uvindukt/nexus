package com.nexus.catalog.domain.repository;

import com.nexus.catalog.domain.model.Category;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for Category persistence operations.
 * Infrastructure adapters (e.g. JPA) implement this contract.
 */
@NoRepositoryBean
public interface CategoryRepository extends ListCrudRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByNameOrSlug(String name, String slug);

    boolean existsDescendant(Long rootId, Long targetId);

}
