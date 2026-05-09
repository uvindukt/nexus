package com.nexus.inventory.application.mapper.messaging;

import com.nexus.inventory.domain.model.Stock;
import com.nexus.inventory.domain.model.StockEvent;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockEventMapper {

    StockEvent toEvent(Stock stock);

}
