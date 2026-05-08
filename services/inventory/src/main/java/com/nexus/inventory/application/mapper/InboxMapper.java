package com.nexus.inventory.application.mapper;

import com.nexus.inventory.domain.model.Inbox;
import com.nexus.inventory.domain.model.InboxArchive;
import com.nexus.shared.InboxEnvelope;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InboxMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void toInbox(InboxEnvelope inboxEnvelope, @MappingTarget Inbox inbox);

    InboxArchive toArchive(Inbox inbox);

    List<InboxArchive> toArchives(List<Inbox> inboxes);

}
