package com.nexus.rag.application.mapper;

import com.nexus.rag.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.rag.domain.model.ProductStockView;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductStockViewMapper {

    ProductStockViewResponse toResponse(ProductStockView productStockView);

    List<ProductStockViewResponse> toResponse(List<ProductStockView> productStockViews);

}
