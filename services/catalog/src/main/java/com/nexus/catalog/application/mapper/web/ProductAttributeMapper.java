package com.nexus.catalog.application.mapper.web;

import com.nexus.catalog.application.dto.web.request.v1.ProductAttributeRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductAttributeResponse;
import com.nexus.catalog.domain.model.ProductAttribute;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductAttributeMapper {

    ProductAttribute toModel(ProductAttributeRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateModel(ProductAttributeRequest request, @MappingTarget ProductAttribute attribute);

    ProductAttributeResponse toResponse(ProductAttribute attribute);

    List<ProductAttributeResponse> toResponses(List<ProductAttribute> attributes);

}
