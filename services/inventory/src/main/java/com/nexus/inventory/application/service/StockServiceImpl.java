package com.nexus.inventory.application.service;

import com.nexus.inventory.application.dto.web.request.v1.StockRequest;
import com.nexus.inventory.application.dto.web.response.v1.StockResponse;
import com.nexus.inventory.application.mapper.web.StockMapper;
import com.nexus.inventory.domain.exception.EntryNotFoundException;
import com.nexus.inventory.domain.model.Stock;
import com.nexus.inventory.domain.model.StockEventType;
import com.nexus.inventory.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final OutboxService outboxService;

    @Transactional
    @Override
    public StockResponse upsertStock(Long productId, StockRequest request) {

        Stock stock = stockRepository.findByProductId(productId)
                .map(existingStock -> {
                    // Product has been initialized via catalog service
                    existingStock.setAvailableQuantity(request.quantity());
                    return existingStock;
                })
                .orElseGet(() -> stockRepository.save(
                        Stock.builder()
                                .productId(productId)
                                .availableQuantity(request.quantity())
                                .reservedQuantity(0)
                                .build()
                ));

        outboxService.stockEvent(stock, StockEventType.STOCK_INITIALIZED);
        return stockMapper.toResponse(stock);

    }

    @Transactional
    @Override
    public StockResponse addToStock(Long productId, StockRequest request) {

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new EntryNotFoundException(productId));

        stock.setAvailableQuantity(stock.getAvailableQuantity() + request.quantity());

        outboxService.stockEvent(stock, StockEventType.STOCK_UPDATED);
        return stockMapper.toResponse(stock);

    }

    @Transactional(readOnly = true)
    @Override
    public StockResponse get(Long productId) {

        return stockRepository.findByProductId(productId)
                .map(stockMapper::toResponse)
                .orElseThrow(() -> new EntryNotFoundException(productId));

    }

    @Transactional(readOnly = true)
    @Override
    public List<StockResponse> getSelected(Long[] productIds) {

        return stockMapper.toResponses(stockRepository.findAllById(List.of(productIds)));

    }

}
