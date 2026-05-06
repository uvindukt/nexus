package com.nexus.catalog.application.mapper.messaging;

import com.nexus.catalog.domain.model.OutboxEnvelope;
import com.nexus.catalog.domain.model.Outbox;
import com.nexus.catalog.domain.model.OutboxArchive;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OutboxEnvelopeMapper {

    OutboxEnvelope toEvent(Outbox outbox);

    OutboxArchive toArchive(Outbox outbox);

}
