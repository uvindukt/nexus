package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.model.InboxArchive;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface InboxArchiveRepository extends ListCrudRepository<InboxArchive, UUID> {


}
