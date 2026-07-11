package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.ProductRequest;
import com.nexus.catalog.application.dto.web.response.v1.BatchOperationType;
import com.nexus.catalog.application.dto.web.response.v1.GenericBatchOperationResponse;
import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.application.mapper.web.ProductMapper;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Brand;
import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.model.ProductEventType;
import com.nexus.catalog.domain.repository.BrandRepository;
import com.nexus.catalog.domain.repository.CachedProductRepository;
import com.nexus.catalog.domain.repository.CategoryRepository;
import com.nexus.catalog.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String SKU = "SKU";
    public static final String SLUG = "Slug";
    private static final String BATCH_PRODUCT_INSERT_SUCCESS = "Product batch insert successful";

    private final ProductRepository productRepository;
    private final CachedProductRepository cachedProductRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final OutboxService outboxService;

    @Transactional
    @Override
    public ProductResponse create(ProductRequest productRequest) {

        validateUniqueness(productRequest);

        Brand brand = brandRepository.findById(productRequest.brandId()).orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));
        Category category = categoryRepository.findById(productRequest.categoryId()).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

        Product product = productMapper.toModel(productRequest);
        product.setBrand(brand);
        product.setCategory(category);

        product = productRepository.save(product);
        outboxService.productEvent(product, ProductEventType.PRODUCT_CREATED);

        return productMapper.toResponse(product);

    }

    @Transactional
    @Override
    public GenericBatchOperationResponse createBatch(List<ProductRequest> productRequest) {

        validateUniqueness(productRequest);

        Set<Long> brandIds = productRequest.stream().map(ProductRequest::brandId).collect(Collectors.toSet());
        List<Brand> brands = brandRepository.findAllById(brandIds);
        if (brands.size() != brandIds.size()) {
            Set<Long> missing = new HashSet<>(brandIds);
            missing.addAll(brands.stream().map(Brand::getId).collect(Collectors.toSet()));
            throw new EntryNotFoundException(Brand.class.getSimpleName(), missing.toString());
        }

        Set<Long> categoryIds = productRequest.stream().map(ProductRequest::categoryId).collect(Collectors.toSet());
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            Set<Long> missing = new HashSet<>(categoryIds);
            missing.addAll(categories.stream().map(Category::getId).collect(Collectors.toSet()));
            throw new EntryNotFoundException(Category.class.getSimpleName(), missing.toString());
        }

        Map<Long, Brand> brandById = brands.stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        Map<Long, Category> categoryById = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        List<Product> products = productRequest.stream()
                .map(request -> {
                    Product product = productMapper.toModel(request);
                    product.setBrand(brandById.get(request.brandId()));
                    product.setCategory(categoryById.get(request.categoryId()));
                    return product;
                })
                .toList();

        List<Product> savedProducts = productRepository.saveAll(products);
        outboxService.productEvent(savedProducts, ProductEventType.PRODUCT_CREATED);
        log.info("Created {} products", savedProducts.size());
        return new GenericBatchOperationResponse(BatchOperationType.INSERT, savedProducts.size(), BATCH_PRODUCT_INSERT_SUCCESS);

    }

    @Transactional
    @Override
    public ProductResponse update(Long productId, ProductRequest productRequest) {

        return productRepository.findById(productId).map(product -> {

            validateUniqueness(productRequest);
            productMapper.updateModel(productRequest, product);

            // Changing Brand scenario
            if (productRequest.brandId() != null && product.getBrand() != null && !product.getBrand().getId().equals(productRequest.brandId())) {
                Brand brand = brandRepository.findById(productRequest.brandId()).orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));
                product.setBrand(brand);
            }

            // Changing Category scenario
            if (productRequest.categoryId() != null && product.getCategory() != null && !product.getCategory().getId().equals(productRequest.categoryId())) {
                Category category = categoryRepository.findById(productRequest.categoryId()).orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
                product.setCategory(category);
            }

            outboxService.productEvent(product, ProductEventType.PRODUCT_UPDATED);
            return productMapper.toResponse(product);

        }).orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));
    }

    @Transactional
    @Override
    public ProductResponse delete(Long productId) {

        return productRepository.findById(productId).map(product -> {

            product.markAsDeleted();
            outboxService.productEvent(product, ProductEventType.PRODUCT_DELETED);
            return productMapper.toResponse(product);

        }).orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional
    @Override
    public ProductResponse activate(Long productId) {

        return productRepository.findById(productId).map(product -> {

            product.activate();
            outboxService.productEvent(product, ProductEventType.PRODUCT_ACTIVATED);
            return productMapper.toResponse(product);

        }).orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse get(Long productId) {

        return cachedProductRepository.findById(productId).orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse get(String sku) {

        return cachedProductRepository.findBySku(sku).orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> getAll() {

        return productMapper.toResponse(productRepository.findAll());

    }

    /**
     * Checks if the Product SKU or Slug from the request object already exists in the DB
     * Throws {@link DuplicateEntryException}
     *
     * @param productRequest {@link ProductRequest} Object with product data
     */
    @NullMarked
    private void validateUniqueness(ProductRequest productRequest) {

        productRepository.findBySkuOrSlug(productRequest.sku(), productRequest.slug()).forEach(product -> {

            if (productRequest.id() == null || !product.getId().equals(productRequest.id())) {

                boolean skuMatch = product.getSku().equalsIgnoreCase(productRequest.sku());
                String field = skuMatch ? SKU : SLUG;
                String value = skuMatch ? productRequest.sku() : productRequest.slug();

                throw new DuplicateEntryException(Product.class.getSimpleName(), field, value);

            }

        });

    }

    /**
     * Checks if the Product SKU or Slug from the request list already exists in the DB or within the list.
     * Throws {@link DuplicateEntryException}
     *
     * @param productRequest List of {@link ProductRequest} Objects with product data
     */
    @NullMarked
    private void validateUniqueness(List<ProductRequest> productRequest) {

        // 1. Catch duplicates within the batch itself
        Set<String> seenSkus = new HashSet<>();
        Set<String> seenSlugs = new HashSet<>();
        for (ProductRequest req : productRequest) {
            if (!seenSkus.add(req.sku())) {
                throw new DuplicateEntryException(Product.class.getSimpleName(), SKU, req.sku());
            }
            if (!seenSlugs.add(req.slug())) {
                throw new DuplicateEntryException(Product.class.getSimpleName(), SLUG, req.slug());
            }
        }

        // 2. Batch-check against existing DB rows
        List<String> skus = productRequest.stream().map(ProductRequest::sku).toList();
        List<String> slugs = productRequest.stream().map(ProductRequest::slug).toList();

        List<Product> conflicts = productRepository.findBySkuInOrSlugIn(skus, slugs);

        Map<Long, ProductRequest> requestsById = productRequest.stream().filter(r -> r.id() != null).collect(Collectors.toMap(ProductRequest::id, Function.identity()));

        conflicts.forEach(product -> {

            ProductRequest matchingRequest = requestsById.get(product.getId());
            boolean isSelf = matchingRequest != null; // same product being updated, not a real conflict

            if (!isSelf) {
                boolean skuMatch = productRequest.stream().anyMatch(r -> product.getSku().equalsIgnoreCase(r.sku()));
                String field = skuMatch ? SKU : SLUG;
                String value = skuMatch ? product.getSku() : product.getSlug();
                throw new DuplicateEntryException(Product.class.getSimpleName(), field, value);
            }

        });

    }

}
