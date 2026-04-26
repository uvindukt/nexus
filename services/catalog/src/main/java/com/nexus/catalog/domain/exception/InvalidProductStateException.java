package com.nexus.catalog.domain.exception;

import lombok.Getter;

@Getter
public class InvalidProductStateException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public InvalidProductStateException(ProductErrorCode errorCode) {
        super("exception.invalid.product-state");
        this.errorCode = errorCode;
    }
}
