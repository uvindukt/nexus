package com.nexus.inventory.domain.exception;

import lombok.Getter;

@Getter
public class OutboxPersistException extends RuntimeException {

    private final Object[] args;

    public OutboxPersistException(Object... args) {
        super("exception.outbox.persist");
        this.args = args;
    }

    public OutboxPersistException(Throwable cause, Object... args) {
        super("exception.outbox.persist", cause);
        this.args = args;
    }

}
