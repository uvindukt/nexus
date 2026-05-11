package com.nexus.inventory.domain.model;

import com.nexus.shared.jdbc.AbstractOutboxCdc;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends AbstractOutboxCdc {

}
