package com.nexus.catalog.application.service;

import com.nexus.catalog.application.dto.web.request.v1.ProductRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.application.mapper.web.ProductMapper;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Brand;
import com.nexus.catalog.domain.model.Category;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.repository.BrandRepository;
import com.nexus.catalog.domain.repository.CategoryRepository;
import com.nexus.catalog.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String SKU = "SKU";
    public static final String SLUG = "Slug";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final OutboxService outboxService;

    @Transactional
    @Override
    public ProductResponse create(ProductRequest productRequest) {

        validateUniqueness(productRequest);

        Brand brand = brandRepository.findById(productRequest.brandId())
                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));

        Product product = productMapper.toModel(productRequest);
        product.setBrand(brand);
        product.setCategory(category);

        product = productRepository.save(product);
        outboxService.save(product);

        return productMapper.toResponse(product);

    }

    @Transactional
    @Override
    public ProductResponse update(Long productId, ProductRequest productRequest) {

        return productRepository.findById(productId)
                .map(product -> {

                    validateUniqueness(productRequest);
                    productMapper.updateModel(productRequest, product);

                    // Changing Brand scenario
                    if (product.getBrand() != null && !product.getBrand().getId().equals(productRequest.brandId())) {
                        Brand brand = brandRepository.findById(productRequest.brandId())
                                .orElseThrow(() -> new EntryNotFoundException(Brand.class.getSimpleName()));
                        product.setBrand(brand);
                    }

                    // Changing Category scenario
                    if (product.getCategory() != null && !product.getCategory().getId().equals(productRequest.categoryId())) {
                        Category category = categoryRepository.findById(productRequest.categoryId())
                                .orElseThrow(() -> new EntryNotFoundException(Category.class.getSimpleName()));
                        product.setCategory(category);
                    }

                    return productMapper.toResponse(product);

                })
                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));
    }

    @Transactional
    @Override
    public ProductResponse delete(Long productId) {

        return productRepository.findById(productId)
                .map(product -> {

                    product.markAsDeleted();
                    return productMapper.toResponse(product);

                })
                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse get(Long productId) {

        return productRepository.findById(productId)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse get(String sku) {

        return productRepository.findBySku(sku)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> getAll() {

        return productMapper.toResponses(productRepository.findAll());

    }

    /**
     * Checks if the Product SKU or Slug from the request object already exists in the DB
     * Throws {@link DuplicateEntryException}
     *
     * @param productRequest {@link ProductRequest} Object with product data
     */
    @NullMarked
    private void validateUniqueness(ProductRequest productRequest) {

        productRepository.findBySkuOrSlug(productRequest.sku(), productRequest.slug())
                .forEach(product -> {

                    if (productRequest.id() == null || !product.getId().equals(productRequest.id())) {

                        boolean skuMatch = product.getSku().equalsIgnoreCase(productRequest.sku());
                        String field = skuMatch ? SKU : SLUG;
                        String value = skuMatch ? productRequest.sku() : productRequest.slug();

                        throw new DuplicateEntryException(Product.class.getSimpleName(), field, value);

                    }

                });

    }

}
