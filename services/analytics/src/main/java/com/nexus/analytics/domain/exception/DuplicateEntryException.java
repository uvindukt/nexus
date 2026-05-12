package com.nexus.analytics.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateEntryException extends RuntimeException {

    private final Object[] args;

    public DuplicateEntryException(Object... args) {
        super("exception.entry.duplicate");
        this.args = args;
    }

    public DuplicateEntryException(Throwable cause, Object... args) {
        super("exception.entry.duplicate", cause);
        this.args = args;
    }

}
