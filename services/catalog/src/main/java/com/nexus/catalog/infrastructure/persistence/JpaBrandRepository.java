package com.nexus.catalog.infrastructure.persistence;

import com.nexus.catalog.domain.model.Brand;
import com.nexus.catalog.domain.repository.BrandRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaBrandRepository extends JpaRepository<Brand, Long>, BrandRepository {

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"products"})
    List<Brand> findAll();

    @Override
    @EntityGraph(attributePaths = {"products"})
    Optional<Brand> findByName(String name);

    @Override
    List<Brand> findByNameIn(Collection<String> names);

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"products"})
    Optional<Brand> findById(Long id);

}
