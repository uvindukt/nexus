package com.nexus.catalog.infrastructure.cache;

import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.application.mapper.web.ProductMapper;
import com.nexus.catalog.domain.model.Product;
import com.nexus.catalog.domain.repository.CachedProductRepository;
import com.nexus.catalog.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisProductRepository implements CachedProductRepository {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, ProductResponse> redisTemplate;
    private final ProductMapper productMapper;

    @Override
    public Optional<ProductResponse> findById(Long id) {

        String key = "catalog:product:" + id;

        ProductResponse cachedProduct = redisTemplate.opsForValue().get(key);
        if (cachedProduct != null) {
            return Optional.of(cachedProduct);
        }

        Optional<Product> product = productRepository.findById(id);
        ProductResponse productResponse = product.map(productMapper::toResponse).orElse(null);
        product.ifPresent(_ -> redisTemplate.opsForValue().set(key, productResponse, jitteredTtl()));

        log.info("Product cached with ID: {}", id);

        if (productResponse == null) {
            return Optional.empty();
        }
        return Optional.of(productResponse);

    }

    @Override
    public Optional<ProductResponse> findBySku(String sku) {

        String key = "catalog:product:sku:" + sku;

        ProductResponse cachedProduct = redisTemplate.opsForValue().get(key);
        if (cachedProduct != null) {
            return Optional.of(cachedProduct);
        }

        Optional<Product> product = productRepository.findBySku(sku);
        ProductResponse productResponse = product.map(productMapper::toResponse).orElse(null);
        product.ifPresent(_ -> redisTemplate.opsForValue().set(key, productResponse, jitteredTtl()));

        log.info("Product cached with SKU: {}", sku);

        if (productResponse == null) {
            return Optional.empty();
        }
        return Optional.of(productResponse);

    }

    /**
     * Calculates a jittered TTL (Time To Live) for cache entries.
     * The base TTL is 30 minutes, with a random jitter of plus or minus 5 minutes
     * to prevent cache stampedes.
     *
     * @return a Duration representing the jittered TTL.
     */
    private Duration jitteredTtl() {
        long jitter = ThreadLocalRandom.current().nextLong(-(5 * 60), ((5 * 60) + 1));
        return Duration.ofSeconds((30 * 60) + jitter);
    }

}
