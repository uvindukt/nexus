package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

    Boolean existsBySlug(String slug);

    Optional<Category> findByName(String name);

}
