package com.nexus.inventory.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.inventory.application.dto.web.InboundView;
import com.nexus.inventory.application.dto.web.OutboundView;
import com.nexus.inventory.application.dto.web.Validate;
import com.nexus.inventory.application.dto.web.request.v1.StockRequest;
import com.nexus.inventory.application.dto.web.response.v1.StockResponse;
import com.nexus.inventory.application.service.StockService;
import com.nexus.inventory.infrastructure.web.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = ApiConstants.Stock.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.Stock.TAG)
@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PatchMapping(ApiConstants.Product + ApiConstants.ProductId)
    @JsonView(OutboundView.Detailed.class)
    @Operation(summary = "openapi.stock.upsert.sum", description = "openapi.stock.upsert.desc")
    ResponseEntity<StockResponse> upsert(@PathVariable Long productId, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody StockRequest stockRequest) {
        return ResponseEntity.ok(stockService.upsertStock(productId, stockRequest));
    }

    @PatchMapping(ApiConstants.Product + ApiConstants.ProductId + ApiConstants.Stock.ADD)
    @JsonView(OutboundView.Detailed.class)
    @Operation(summary = "openapi.stock.addToStock.sum", description = "openapi.stock.addToStock.desc")
    ResponseEntity<StockResponse> addToStock(@PathVariable Long productId, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody StockRequest stockRequest) {
        return ResponseEntity.ok(stockService.addToStock(productId, stockRequest));
    }

    @GetMapping(ApiConstants.Product + ApiConstants.ProductId)
    @JsonView(OutboundView.Detailed.class)
    @Operation(summary = "openapi.stock.get.sum", description = "openapi.stock.get.desc")
    ResponseEntity<StockResponse> get(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.get(productId));
    }

    @GetMapping(ApiConstants.Product)
    @JsonView(OutboundView.Detailed.class)
    @Operation(summary = "openapi.stock.getSelected.sum", description = "openapi.stock.getSelected.desc")
    ResponseEntity<List<StockResponse>> getSelected(@RequestParam Long[] productIds) {
        return ResponseEntity.ok(stockService.getSelected(productIds));
    }

}
