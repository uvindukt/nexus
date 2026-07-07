package com.nexus.rag.domain.exception;

import lombok.Getter;

@Getter
public class SearchPipelineException extends RuntimeException {

    private final Object[] args;

    public SearchPipelineException(Object... args) {
        super("exception.service.search-pipeline");
        this.args = args;
    }

    public SearchPipelineException(Throwable cause, Object... args) {
        super("exception.service.search-pipeline", cause);
        this.args = args;
    }
}
