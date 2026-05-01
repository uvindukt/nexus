package com.nexus.catalog.application.service.impl;

import com.nexus.catalog.application.dto.web.request.v1.ProductAttributeRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductAttributeResponse;
import com.nexus.catalog.application.mapper.web.ProductAttributeMapper;
import com.nexus.catalog.application.service.ProductAttributeService;
import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.model.ProductAttribute;
import com.nexus.catalog.domain.repository.ProductAttributeRepository;
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
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ProductAttributeResponse create(ProductAttributeRequest request) {

        validateUniqueness(request);

        return productRepository.findById(request.productId())
                .map(product -> {

                    ProductAttribute attribute = productAttributeMapper.toModel(request);
                    attribute.setProduct(product);

                    return productAttributeMapper.toResponse(productAttributeRepository.save(attribute));

                })
                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));

    }

    @Transactional
    @Override
    public ProductAttributeResponse update(Long id, ProductAttributeRequest request) {

        return productAttributeRepository.findById(id)
                .map(attribute -> {

                    productAttributeMapper.updateModel(request, attribute);

                    // If changing the Product in ProductAttribute
                    if (!attribute.getProduct().getId().equals(request.productId())) {
                        Product product = productRepository.findById(request.productId())
                                .orElseThrow(() -> new EntryNotFoundException(Product.class.getSimpleName()));
                        attribute.setProduct(product);
                    }

                    return productAttributeMapper.toResponse(attribute);

                })
                .orElseThrow(() -> new EntryNotFoundException(ProductAttribute.class.getSimpleName()));

    }

    @Transactional
    @Override
    public ProductAttributeResponse delete(Long id) {

        return productAttributeRepository.findById(id)
                .map(attribute -> {
                    productAttributeRepository.delete(attribute);
                    return productAttributeMapper.toResponse(attribute);
                })
                .orElseThrow(() -> new EntryNotFoundException(ProductAttribute.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductAttributeResponse get(Long id) {

        return productAttributeRepository.findById(id)
                .map(productAttributeMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(ProductAttribute.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public ProductAttributeResponse get(String key) {

        return productAttributeRepository.findByKey(key)
                .map(productAttributeMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(ProductAttribute.class.getSimpleName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductAttributeResponse> getAll() {

        return productAttributeMapper.toResponses(productAttributeRepository.findAll());

    }

    /**
     * Checks if the ProductAttribute Key from the request, already exists in the DB
     * Throws {@link DuplicateEntryException}
     *
     * @param request {@link ProductAttributeRequest} object with ProductAttribute data
     */
    @NullMarked
    private void validateUniqueness(ProductAttributeRequest request) {

        productAttributeRepository.findByKey(request.key())
                .ifPresent(attribute -> {
                            if (request.id() == null || !attribute.getId().equals(request.id())) {
                                throw new DuplicateEntryException(ProductAttribute.class.getSimpleName(), request.key(), request.value());
                            }
                        }
                );

    }

}
