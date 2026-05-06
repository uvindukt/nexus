package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.InboxArchive;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface InboxArchiveRepository extends ListCrudRepository<InboxArchive, UUID> {


}
