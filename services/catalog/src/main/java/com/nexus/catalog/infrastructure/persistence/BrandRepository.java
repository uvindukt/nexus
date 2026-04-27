package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Brand;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"products"})
    List<Brand> findAll();

    @EntityGraph(attributePaths = {"products"})
    Optional<Brand> findByName(String name);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"products"})
    Optional<Brand> findById(Long id);


}
