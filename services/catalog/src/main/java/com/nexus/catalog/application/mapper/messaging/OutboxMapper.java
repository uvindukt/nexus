package com.nexus.catalog.application.mapper.messaging;

import com.nexus.catalog.application.dto.messaging.v1.OutboxEvent;
import com.nexus.catalog.domain.model.Outbox;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OutboxMapper {

    OutboxEvent toOutboxEvent(Outbox outbox);

}
