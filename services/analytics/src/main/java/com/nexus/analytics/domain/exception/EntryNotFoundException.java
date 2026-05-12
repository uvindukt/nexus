package com.nexus.analytics.domain.exception;

import lombok.Getter;

@Getter
public class EntryNotFoundException extends RuntimeException {

    private final Object[] args;


    public EntryNotFoundException(Object... args) {
        super("exception.entry.not-found");
        this.args = args;
    }

    public EntryNotFoundException(Throwable cause, Object... args) {
        super("exception.entry.not-found", cause);
        this.args = args;
    }

}
