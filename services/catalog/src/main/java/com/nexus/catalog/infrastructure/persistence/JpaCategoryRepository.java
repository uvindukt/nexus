package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.domain.repository.CategoryRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository {

    @Override
    @EntityGraph(attributePaths = {"parent", "subCategories"})
    List<Category> findByNameOrSlug(String name, String slug);

    @Override
    @EntityGraph(attributePaths = {"parent", "subCategories"})
    Optional<Category> findByName(String name);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"parent", "subCategories"})
    List<Category> findAll();

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"parent", "subCategories"})
    Optional<Category> findById(Long id);

}
