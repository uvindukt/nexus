package com.nexus.inventory.application.mapper;

import com.nexus.inventory.application.dto.web.response.v1.StockResponse;
import com.nexus.inventory.domain.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockMapper {

    StockResponse toResponse(Stock stock);

}
