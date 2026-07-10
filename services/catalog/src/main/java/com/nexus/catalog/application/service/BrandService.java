package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.BrandRequest;
import com.nexus.catalog.application.dto.web.response.v1.BrandResponse;
import com.nexus.catalog.domain.model.Brand;

import java.util.List;

public interface BrandService {

    /**
     * Creates a {@link Brand} object and persist in database
     *
     * @param brandRequest {@link BrandRequest} object containing brand data
     * @return {@link BrandResponse} object containing created brand data
     */
    BrandResponse create(BrandRequest brandRequest);

    /**
     * Creates multiple {@link Brand} objects and persist in database
     *
     * @param brandRequest {@link List<BrandRequest>} object containing brand data
     * @return {@link List<BrandResponse>} object containing created brands data
     */
    List<BrandResponse> createBatch(List<BrandRequest> brandRequest);

    /**
     * Updates a {@link Brand} object with provided data and persist in database
     *
     * @param brandId      Brand ID
     * @param brandRequest {@link BrandRequest} object containing brand data
     * @return {@link BrandResponse} object containing updated brand data
     */
    BrandResponse update(Long brandId, BrandRequest brandRequest);

    /**
     * Deactivates a {@link Brand} with provided ID
     *
     * @param brandId Brand ID
     * @return {@link BrandResponse} object containing brand data
     */
    BrandResponse delete(Long brandId);

    /**
     * Retrieves a {@link Brand} with provided ID
     *
     * @param brandId Brand ID
     * @return {@link BrandResponse} object containing brand data
     */
    BrandResponse get(Long brandId);

    /**
     * Retrieves a {@link Brand} with provided Name
     *
     * @param brandName Brand Name
     * @return {@link BrandResponse} object containing brand data
     */
    BrandResponse get(String brandName);

    /**
     * Retrieves all {@link Brand} objects
     *
     * @return {@link List<BrandResponse>} object containing all brands
     */
    List<BrandResponse> getAll();

}
