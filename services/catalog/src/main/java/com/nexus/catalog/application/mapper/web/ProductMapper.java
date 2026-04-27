package com.nexus.catalog.application.mapper.web;

import com.nexus.catalog.application.dto.web.request.v1.ProductRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.domain.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoryMapper.class, BrandMapper.class, ProductAttributeMapper.class}
)
public interface ProductMapper {

    Product toModel(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateModel(ProductRequest request, @MappingTarget Product product);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponses(List<Product> products);

}
