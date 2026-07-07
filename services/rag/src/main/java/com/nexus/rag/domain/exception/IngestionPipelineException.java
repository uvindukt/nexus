package com.nexus.rag.domain.exception;

import lombok.Getter;

@Getter
public class IngestionPipelineException extends RuntimeException {

    private final Object[] args;

    public IngestionPipelineException(Object... args) {
        super("exception.service.ingestion-pipeline");
        this.args = args;
    }

    public IngestionPipelineException(Throwable cause, Object... args) {
        super("exception.service.ingestion-pipeline", cause);
        this.args = args;
    }

}
