package com.nexus.inventory.application.mapper;

import com.nexus.inventory.application.dto.web.response.v1.StockReservationResponse;
import com.nexus.inventory.domain.model.StockReservation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockReservationMapper {

    StockReservationResponse toResponse(StockReservation stockReservation);

}
