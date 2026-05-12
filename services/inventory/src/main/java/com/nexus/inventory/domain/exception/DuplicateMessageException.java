package com.nexus.inventory.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateMessageException extends RuntimeException {

    private final Object[] args;

    public DuplicateMessageException(Object... args) {
        super("duplicate.message.exception");
        this.args = args;
    }

    public DuplicateMessageException(Throwable cause, Object... args) {
        super("duplicate.message.exception", cause);
        this.args = args;
    }

}
