package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.domain.repository.CategoryRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Walks the entire subtree rooted at {@code rootId} in a single round-trip
     * using a recursive CTE, then checks whether {@code targetId} appears in it.
     *
     * PostgreSQL evaluates the CTE once; no additional queries are issued
     * regardless of how deep or wide the category tree is.
     */
    @Query(nativeQuery = true, value = """
            WITH RECURSIVE descendants AS (
                SELECT id FROM category WHERE id = :rootId
                UNION ALL
                SELECT c.id FROM category c
                    INNER JOIN descendants d ON c.parent_id = d.id
            )
            SELECT COUNT(*) > 0 FROM descendants WHERE id = :targetId
            """)
    boolean existsDescendant(@Param("rootId") Long rootId, @Param("targetId") Long targetId);

}
