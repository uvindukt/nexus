package com.nexus.inventory.domain.model;

import java.math.BigDecimal;

public record ProductPayload(
        Long id,
        BigDecimal price
) {
}
