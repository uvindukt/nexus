package com.nexus.inventory.application.service;

import com.nexus.inventory.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public void addStock(Long productId) {

    }

    @Override
    public void updateStock(Long productId, BigDecimal price) {

    }

}
