package com.nexus.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.InboundView;
import com.nexus.catalog.application.dto.web.OutboundView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.request.v1.ProductAttributeRequest;
import com.nexus.catalog.application.dto.web.response.v1.ProductAttributeResponse;
import com.nexus.catalog.application.service.ProductAttributeService;
import com.nexus.catalog.infrastructure.web.constant.ApiConstants;
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
@RequestMapping(value = ApiConstants.ProductAttributes.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.ProductAttributes.TAG)
@RequiredArgsConstructor
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;

    @PostMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product-attribute.create.sum", description = "openapi.product-attribute.create.desc")
    public ResponseEntity<ProductAttributeResponse> create(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody ProductAttributeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productAttributeService.create(request));
    }

    @GetMapping
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.product-attribute.getAll.sum", description = "openapi.product-attribute.getAll.desc")
    public ResponseEntity<List<ProductAttributeResponse>> getAll() {
        return ResponseEntity.ok(productAttributeService.getAll());
    }

    @GetMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product-attribute.findById.sum", description = "openapi.product-attribute.findById.desc")
    public ResponseEntity<ProductAttributeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productAttributeService.get(id));
    }

    @PutMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.product-attribute.update.sum", description = "openapi.product-attribute.update.desc")
    public ResponseEntity<ProductAttributeResponse> update(@PathVariable Long id, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody ProductAttributeRequest request) {
        return ResponseEntity.ok(productAttributeService.update(id, request));
    }

    @DeleteMapping(ApiConstants.ID)
    @Operation(summary = "openapi.product-attribute.delete.sum", description = "openapi.product-attribute.delete.desc")
    public ResponseEntity<ProductAttributeResponse> delete(@PathVariable Long id) {
        productAttributeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
