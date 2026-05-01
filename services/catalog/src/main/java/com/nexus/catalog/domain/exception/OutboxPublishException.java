package com.nexus.catalog.domain.exception;

import lombok.Getter;

@Getter
public class OutboxPublishException extends RuntimeException {

    private final Object[] args;


    public OutboxPublishException(Object... args) {
        super("exception.failed.outbox");
        this.args = args;
    }

}
