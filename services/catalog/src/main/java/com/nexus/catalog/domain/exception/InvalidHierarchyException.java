package com.nexus.catalog.domain.exception;

import lombok.Getter;

@Getter
public class InvalidHierarchyException extends RuntimeException {

    private final Object[] args;

    public InvalidHierarchyException(Object... args) {
        super("exception.invalid.hierarchy");
        this.args = args;
    }

}
