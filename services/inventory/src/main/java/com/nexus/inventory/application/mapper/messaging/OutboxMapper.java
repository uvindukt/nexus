package com.nexus.inventory.application.mapper.messaging;

import com.nexus.inventory.domain.model.Outbox;
import com.nexus.inventory.domain.model.OutboxArchive;
import com.nexus.shared.outbox.OutboxEnvelope;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OutboxMapper {

    OutboxEnvelope toEvent(Outbox outbox);

    OutboxArchive toArchive(Outbox outbox);

}
