package com.nexus.rag.domain.exception;

import lombok.Getter;

@Getter
public class RagServiceException extends RuntimeException {

    private final Object[] args;

    public RagServiceException(Object... args) {
        super("exception.service.rag");
        this.args = args;
    }

    public RagServiceException(Throwable cause, Object... args) {
        super("exception.service.rag", cause);
        this.args = args;
    }
}
