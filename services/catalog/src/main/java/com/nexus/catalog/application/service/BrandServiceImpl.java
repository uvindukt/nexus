package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.BrandRequest;
import com.nexus.catalog.application.dto.web.response.v1.BatchOperationType;
import com.nexus.catalog.application.dto.web.response.v1.BrandResponse;
import com.nexus.catalog.application.dto.web.response.v1.GenericBatchOperationResponse;
import com.nexus.catalog.application.mapper.web.BrandMapper;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Brand;
import com.nexus.catalog.domain.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private static final String NAME = "Name";
    private static final String BATCH_BRAND_INSERT_SUCCESS = "Brand batch insert successful";

    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;

    @Transactional
    @Override
    public BrandResponse create(BrandRequest brandRequest) {
        validateUniqueness(brandRequest);
        return brandMapper.toResponse(brandRepository.save(brandMapper.toModel(brandRequest)));
    }

    @Transactional
    @Override
    public GenericBatchOperationResponse createBatch(List<BrandRequest> brandRequest) {
        validateBatchUniqueness(brandRequest);
        List<Brand> brands = brandRepository.saveAll(brandMapper.toModel(brandRequest));
        log.info("Created {} brands", brands.size());
        return new GenericBatchOperationResponse(BatchOperationType.INSERT, brands.size(), BATCH_BRAND_INSERT_SUCCESS);
    }

    @Transactional
    @Override
    public BrandResponse update(Long brandId, BrandRequest brandRequest) {

        return brandRepository.findById(brandId)
                .map(brand -> {

                    validateUniqueness(brandRequest);
                    brandMapper.updateModel(brandRequest, brand);
                    return brandMapper.toResponse(brand);

                })
                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));

    }

    @Transactional
    @Override
    public BrandResponse delete(Long brandId) {

        return brandRepository.findById(brandId)
                .map(brand -> {

                    brand.setActive(false);
                    return brandMapper.toResponse(brand);

                })
                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public BrandResponse get(Long brandId) {

        return brandRepository.findById(brandId)
                .map(brandMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public BrandResponse get(String brandName) {

        return brandRepository.findByName(brandName)
                .map(brandMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<BrandResponse> getAll() {
        return brandMapper.toResponse(brandRepository.findAll());
    }

    /**
     * Checks if the Brand Name from the request object already exists in the DB
     * Throws {@link DuplicateEntryException}
     *
     * @param brandRequest {@link BrandRequest} Object with brand data
     */
    @NullMarked
    private void validateUniqueness(BrandRequest brandRequest) {

        brandRepository.findByName(brandRequest.name())
                .ifPresent(brand -> {
                    if (brandRequest.id() == null || !brand.getId().equals(brandRequest.id())) {
                        throw new DuplicateEntryException(Brand.class.getSimpleName(), NAME, brandRequest.name());
                    }
                });

    }

    /**
     * Checks if the Brand Names from the request object already exists in the DB
     * Throws {@link DuplicateEntryException}
     *
     * @param brandRequest List of {@link BrandRequest} Objects with brand data
     */
    private void validateBatchUniqueness(List<BrandRequest> brandRequest) {
        List<String> names = brandRequest.stream().map(BrandRequest::name).toList();

        // duplicates within the incoming batch itself
        Set<String> seen = new HashSet<>();
        names.stream()
                .filter(n -> !seen.add(n))
                .findFirst()
                .ifPresent(dup -> {
                    throw new DuplicateEntryException(Brand.class.getSimpleName(), NAME, dup);
                });

        // duplicates against what's already persisted — single query, not N
        brandRepository.findByNameIn(names).stream()
                .findFirst()
                .ifPresent(existing -> {
                    throw new DuplicateEntryException(Brand.class.getSimpleName(), NAME, existing.getName());
                });
    }

}
