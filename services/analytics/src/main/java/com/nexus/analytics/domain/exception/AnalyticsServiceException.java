package com.nexus.analytics.domain.exception;

import lombok.Getter;

@Getter
public class AnalyticsServiceException extends RuntimeException {

    private final Object[] args;

    public AnalyticsServiceException(Object... args) {
        super("exception.service.analytics");
        this.args = args;
    }

    public AnalyticsServiceException(Throwable cause, Object... args) {
        super("exception.service.analytics", cause);
        this.args = args;
    }

}
