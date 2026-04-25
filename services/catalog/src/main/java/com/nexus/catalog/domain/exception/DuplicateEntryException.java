package com.nexus.catalog.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateEntryException extends RuntimeException {

    private final Object[] args;

    public DuplicateEntryException(Object... args) {
        super("exception.entry.duplicate");
        this.args = args;
    }

}
