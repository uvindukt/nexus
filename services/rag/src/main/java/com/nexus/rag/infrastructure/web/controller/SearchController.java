package com.nexus.rag.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.rag.application.dto.web.OutboundView;
import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;
import com.nexus.rag.application.service.ProductSearchService;
import com.nexus.rag.infrastructure.web.constant.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiConstants.RAG.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.RAG.TAG)
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService productSearchService;

    @GetMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.rag.search.sum", description = "openapi.rag.search.desc")
    public ResponseEntity<ProductSearchResponse> search(@RequestParam("query") String query) {
        return ResponseEntity.ok(productSearchService.search(query));
    }

}
