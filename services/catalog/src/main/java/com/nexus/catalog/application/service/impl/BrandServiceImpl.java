package com.nexus.catalog.application.service.impl;

import com.nexus.catalog.application.dto.web.request.v1.BrandRequest;
import com.nexus.catalog.application.dto.web.response.v1.BrandResponse;
import com.nexus.catalog.application.mapper.web.BrandMapper;
import com.nexus.catalog.application.service.BrandService;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Brand;
import com.nexus.catalog.domain.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    public static final String NAME = "Name";

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
        return brandMapper.toResponses(brandRepository.findAll());
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

}
