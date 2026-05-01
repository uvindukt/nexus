package com.nexus.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.InboundView;
import com.nexus.catalog.application.dto.web.OutboundView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.request.v1.ProductRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import com.nexus.catalog.application.service.ProductService;
import com.nexus.catalog.infrastructure.web.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.Products.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.Products.TAG)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product.create.sum", description = "openapi.product.create.desc")
    public ResponseEntity<ProductResponse> create(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequest));
    }

    @GetMapping
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.product.findAll.sum", description = "openapi.product.findAll.desc")
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product.findById.sum", description = "openapi.product.findById.desc")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PutMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product.update.sum", description = "openapi.product.update.desc")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.update(id, productRequest));
    }

    @DeleteMapping(ApiConstants.ID)
    @Operation(summary = "openapi.product.delete.sum", description = "openapi.product.delete.desc")
    public ResponseEntity<ProductResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(productService.delete(id));
    }

}
