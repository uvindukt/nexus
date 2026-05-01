package com.nexus.catalog.application.mapper.messaging;

import com.nexus.catalog.application.dto.messaging.publisher.v1.ProductPublisherEvent;
import com.nexus.catalog.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductEventMapper {

    ProductPublisherEvent toEvent(Product product);

}
