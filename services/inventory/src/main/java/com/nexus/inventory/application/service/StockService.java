package com.nexus.inventory.application.service;

import java.math.BigDecimal;

public interface StockService {

    void addStock(Long productId);

    void updateStock(Long productId, BigDecimal price);

}
