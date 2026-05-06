package com.nexus.catalog.domain.model;

import java.math.BigDecimal;

public record ProductEvent(
        Long id,
        BigDecimal price
) {
}
