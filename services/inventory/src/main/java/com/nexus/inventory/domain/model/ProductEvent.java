package com.nexus.inventory.domain.model;

import java.math.BigDecimal;

public record ProductEvent(
        Long id,
        BigDecimal price
) {
}
