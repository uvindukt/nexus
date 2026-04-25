package com.nexus.catalog.application.service.impl;

import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;
import com.nexus.catalog.application.mapper.web.CategoryMapper;
import com.nexus.catalog.application.service.CategoryService;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.infrastructure.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new DuplicateEntryException(Category.class.getSimpleName(), "Name", categoryRequest.name());
        }

        if (categoryRepository.existsBySlug(categoryRequest.slug())) {
            throw new DuplicateEntryException(Category.class.getSimpleName(), "Slug", categoryRequest.slug());
        }

        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toModel(categoryRequest)));

    }

    @Transactional
    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest) {

        categoryRepository.findById(categoryRequest.id()).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toModel(categoryRequest)));

    }

    @Transactional
    @Override
    public CategoryResponse deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
        category.setActive(false);

        return categoryMapper.toResponse(categoryRepository.save(category));

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
        return categoryMapper.toResponse(category);

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategory(String categoryName) {

        Category category = categoryRepository.findByName(categoryName).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
        return categoryMapper.toResponse(category);

    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getCategories() {

        return categoryMapper.toResponses(categoryRepository.findAll());

    }

}
