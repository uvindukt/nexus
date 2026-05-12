package com.nexus.inventory.domain.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final Object[] args;

    public InsufficientStockException(Object... args) {
        super("stock.insufficient.exception");
        this.args = args;
    }

    public InsufficientStockException(Throwable cause, Object... args) {
        super("stock.insufficient.exception", cause);
        this.args = args;
    }

}
