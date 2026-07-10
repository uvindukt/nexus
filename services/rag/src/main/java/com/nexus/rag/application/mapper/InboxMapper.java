package com.nexus.rag.application.mapper;

import com.nexus.rag.domain.model.consumer.Inbox;
import com.nexus.rag.domain.model.consumer.InboxArchive;
import com.nexus.shared.common.InboxEnvelope;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
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
