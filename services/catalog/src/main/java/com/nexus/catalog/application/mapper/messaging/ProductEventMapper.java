package com.nexus.catalog.application.mapper.messaging;

import com.nexus.catalog.application.dto.messaging.v1.ProductCreatedEventV1;
import com.nexus.catalog.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEventMapper {

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "occurredAt", expression = "java(java.time.Instant.now())")
    ProductCreatedEventV1 toCreatedEventV1(Product product);

}
