package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;

import java.util.List;

public interface CategoryService {

    /**
     * Creates a {@link com.nexus.catalog.domain.model.Category} object and persist in database
     *
     * @param categoryRequest {@link CategoryRequest} object containing category data
     * @return {@link CategoryResponse} object containing created category data
     */
    CategoryResponse create(CategoryRequest categoryRequest);

    /**
     * Creates multiple {@link com.nexus.catalog.domain.model.Category} objects and persist in database
     *
     * @param categoryRequest {@link java.util.List<CategoryRequest>} object containing categories data
     * @return {@link java.util.List<CategoryResponse>} object containing created categories data
     */
    List<CategoryResponse> createBatch(List<CategoryRequest> categoryRequest);

    /**
     * Updates a {@link com.nexus.catalog.domain.model.Category} object with provided data and persist in database
     *
     * @param categoryId      Category ID
     * @param categoryRequest {@link CategoryRequest} object containing category data
     * @return {@link CategoryResponse} object containing updated category data
     */
    CategoryResponse update(Long categoryId, CategoryRequest categoryRequest);

    /**
     * Deactivates a {@link com.nexus.catalog.domain.model.Category} with provided ID
     *
     * @param categoryId Category ID
     * @return {@link CategoryResponse} object containing category data
     */
    CategoryResponse delete(Long categoryId);

    /**
     * Retrieves a {@link com.nexus.catalog.domain.model.Category} with provided ID
     *
     * @param categoryId Category ID
     * @return {@link CategoryResponse} object containing category data
     */
    CategoryResponse get(Long categoryId);

    /**
     * Retrieves a {@link com.nexus.catalog.domain.model.Category} with provided Name
     *
     * @param categoryName Category Name
     * @return {@link CategoryResponse} object containing category data
     */
    CategoryResponse get(String categoryName);

    /**
     * Retrieves all {@link com.nexus.catalog.domain.model.Category} objects
     *
     * @return {@link List<CategoryResponse>} object containing all categories
     */
    List<CategoryResponse> getAll();

}
