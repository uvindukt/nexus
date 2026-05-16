package com.nexus.analytics.application.mapper;

import com.nexus.analytics.domain.model.Inbox;
import com.nexus.analytics.domain.model.InboxArchive;
import com.nexus.shared.common.InboxEnvelope;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN,
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
