package com.nexus.analytics.application.mapper;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.domain.model.ProductStockView;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductStockViewMapper {

    ProductStockViewResponse toResponse(ProductStockView productStockView);

}
