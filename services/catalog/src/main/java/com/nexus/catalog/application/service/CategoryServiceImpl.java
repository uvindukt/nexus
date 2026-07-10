package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;
import com.nexus.catalog.application.mapper.web.CategoryMapper;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.exception.InvalidHierarchyException;
import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    public static final String NAME = "Name";
    public static final String SLUG = "Slug";

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {

        validateUniqueness(categoryRequest);
        var category = categoryMapper.toModel(categoryRequest);

        if (categoryRequest.parentId() != null) {
            var parentCategory = categoryRepository.findById(categoryRequest.parentId())
                    .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
            category.setParent(parentCategory);
        }

        return categoryMapper.toResponse(categoryRepository.save(category));

    }

    @Transactional
    @Override
    public List<CategoryResponse> createBatch(List<CategoryRequest> categoryRequest) {

        validateUniqueness(categoryRequest);

        Set<Long> parentIds = categoryRequest.stream()
                .map(CategoryRequest::parentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Category> parentsById = categoryRepository.findAllById(parentIds).stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        // Filter out missing parent IDs
        if (parentsById.size() != parentIds.size()) {
            Set<Long> missing = new HashSet<>(parentIds);
            missing.removeAll(parentsById.keySet());
            throw new EntryNotFoundException(Category.class.getSimpleName(), missing.toString());
        }

        List<Category> categories = categoryRequest.stream().map(singleCategoryRequest -> {
            Category category = categoryMapper.toModel(singleCategoryRequest);
            if (singleCategoryRequest.parentId() != null) {
                category.setParent(parentsById.get(singleCategoryRequest.parentId()));
            }
            return category;
        }).toList();

        return categoryMapper.toResponse(categoryRepository.saveAll(categories));

    }

    @Transactional
    @Override
    public CategoryResponse update(Long categoryId, CategoryRequest categoryRequest) {

        return categoryRepository.findById(categoryId).map(category -> {

            validateUniqueness(categoryRequest);
            categoryMapper.updateModel(categoryRequest, category);

            if (categoryRequest.parentId() != null) {
                validateHierarchy(category, categoryRequest.parentId());
                var parentCategory = categoryRepository.findById(categoryRequest.parentId())
                        .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
                category.setParent(parentCategory);
            } else {
                category.setParent(null);
            }

            return categoryMapper.toResponse(category);

        }).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

    }

    @Transactional
    @Override
    public CategoryResponse delete(Long categoryId) {

        return categoryRepository.findById(categoryId).map(category -> {
            category.setActive(false);
            return categoryMapper.toResponse(category);
        }).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse get(Long categoryId) {

        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse get(String categoryName) {

        return categoryRepository.findByName(categoryName)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getAll() {

        return categoryMapper.toResponse(categoryRepository.findAll());

    }

    /**
     * Checks if the Category Name or Slug from the request object already exists in the DB.
     * Throws {@link DuplicateEntryException}
     *
     * @param categoryRequest {@link CategoryRequest} Object with category data
     */
    @NullMarked
    private void validateUniqueness(CategoryRequest categoryRequest) {

        var conflict = categoryRepository.findByNameOrSlug(categoryRequest.name(), categoryRequest.slug());

        conflict.forEach(category -> {

            if (categoryRequest.id() == null || !category.getId().equals(categoryRequest.id())) {
                boolean nameMatch = category.getName().equalsIgnoreCase(categoryRequest.name());
                String field = nameMatch ? NAME : SLUG;
                String value = nameMatch ? categoryRequest.name() : categoryRequest.slug();

                throw new DuplicateEntryException(Category.class.getSimpleName(), field, value);
            }

        });

    }

    /**
     * Checks if the Category Name or Slug from the batch of request objects already exists in the DB
     * or is duplicated within the batch itself.
     * Throws {@link DuplicateEntryException}
     *
     * @param categoryRequest List of {@link CategoryRequest} Objects with category data
     */
    @NullMarked
    private void validateUniqueness(List<CategoryRequest> categoryRequest) {

        // 1. Catch duplicates within the batch itself
        Set<String> seenNames = new HashSet<>();
        Set<String> seenSlugs = new HashSet<>();
        for (CategoryRequest req : categoryRequest) {
            if (!seenNames.add(req.name().toLowerCase())) {
                throw new DuplicateEntryException(Category.class.getSimpleName(), NAME, req.name());
            }
            if (!seenSlugs.add(req.slug().toLowerCase())) {
                throw new DuplicateEntryException(Category.class.getSimpleName(), SLUG, req.slug());
            }
        }

        // 2. Batch-check against existing DB rows
        List<String> names = categoryRequest.stream().map(CategoryRequest::name).toList();
        List<String> slugs = categoryRequest.stream().map(CategoryRequest::slug).toList();

        List<Category> conflicts = categoryRepository.findByNameInOrSlugIn(names, slugs);

        Map<Long, CategoryRequest> requestsById = categoryRequest.stream()
                .filter(r -> r.id() != null)
                .collect(Collectors.toMap(CategoryRequest::id, Function.identity()));

        conflicts.forEach(category -> {

            CategoryRequest matchingRequest = requestsById.get(category.getId());
            boolean isSelf = matchingRequest != null; // same category being updated, not a real conflict

            if (!isSelf) {
                boolean nameMatch = categoryRequest.stream().anyMatch(r -> category.getName().equalsIgnoreCase(r.name()));
                String field = nameMatch ? NAME : SLUG;
                String value = nameMatch ? category.getName() : category.getSlug();
                throw new DuplicateEntryException(Category.class.getSimpleName(), field, value);
            }

        });

    }

    /**
     * Validates {@link Category} parent-child relations before reparenting.
     *
     * <p>Two invariants must hold:
     * <ol>
     *   <li>A category cannot be its own parent.</li>
     *   <li>A category cannot be reparented to one of its own descendants
     *       (that would create a cycle).</li>
     * </ol>
     *
     * <p>The CTE in {@link CategoryRepository#existsDescendant} covers both
     * cases: the recursive walk starts at {@code category.getId()} (which
     * includes the node itself), so a self-reference and any deeper descendant
     * are caught in a single round-trip.
     *
     * @param category the category being updated
     * @param parentId the proposed new parent ID
     */
    @NullMarked
    private void validateHierarchy(Category category, Long parentId) {

        if (categoryRepository.existsDescendant(category.getId(), parentId)) {
            throw new InvalidHierarchyException(Category.class.getSimpleName());
        }

    }

}