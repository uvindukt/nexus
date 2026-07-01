package com.nexus.rag.domain.model.consumer;

import com.nexus.shared.jdbc.AbstractInbox;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.NullMarked;

@SuperBuilder
@NullMarked
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inbox extends AbstractInbox {


}
