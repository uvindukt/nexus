package com.nexus.gateway.infrastructure.web.controller;

import com.nexus.gateway.infrastructure.web.constant.ApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ApiConstants.FALLBACK_URL)
public class FallbackController {

    @GetMapping(ApiConstants.Catalog.BASE)
    public ResponseEntity<Map<String, Object>> catalogFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        ApiConstants.ERROR, ApiConstants.Catalog.ERROR,
                        ApiConstants.MESSAGE, ApiConstants.Catalog.MESSAGE
                ));
    }

    @GetMapping(ApiConstants.Inventory.BASE)
    public ResponseEntity<Map<String, Object>> inventoryFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(ApiConstants.ERROR, ApiConstants.Inventory.ERROR));
    }

    @GetMapping(ApiConstants.Analytics.BASE)
    public ResponseEntity<Map<String, Object>> analyticsFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(ApiConstants.ERROR, ApiConstants.Analytics.ERROR));
    }

    @GetMapping(ApiConstants.Rag.BASE)
    public ResponseEntity<Map<String, Object>> ragFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        ApiConstants.ERROR, ApiConstants.Rag.ERROR,
                        ApiConstants.MESSAGE, ApiConstants.Rag.MESSAGE
                ));
    }

}
