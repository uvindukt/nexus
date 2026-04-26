package com.nexus.catalog.domain.exception;

import lombok.Getter;

@Getter
public class EntryNotFoundException extends RuntimeException {

    private final Object[] args;

    public EntryNotFoundException(Object... args) {
        super("exception.entry.not-found");
        this.args = args;
    }

}
