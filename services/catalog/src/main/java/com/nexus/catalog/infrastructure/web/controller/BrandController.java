package com.nexus.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.InboundView;
import com.nexus.catalog.application.dto.web.OutboundView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.request.v1.BrandRequest;
import com.nexus.catalog.application.dto.web.response.v1.BrandResponse;
import com.nexus.catalog.application.service.BrandService;
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
@RequestMapping(value = ApiConstants.Brands.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.Brands.TAG)
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.brand.create.sum", description = "openapi.brand.create.desc")
    public ResponseEntity<BrandResponse> create(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody BrandRequest brandRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.create(brandRequest));
    }

    @GetMapping
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.brand.getAll.sum", description = "openapi.brand.getAll.desc")
    public ResponseEntity<List<BrandResponse>> getAll() {
        return ResponseEntity.ok(brandService.getAll());
    }

    @GetMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.brand.findById.sum", description = "openapi.brand.findById.desc")
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.get(id));
    }

    @PutMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.brand.update.sum", description = "openapi.brand.update.desc")
    public ResponseEntity<BrandResponse> update(@PathVariable Long id, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody BrandRequest brandRequest) {
        return ResponseEntity.ok(brandService.update(id, brandRequest));
    }

    @DeleteMapping(ApiConstants.ID)
    @Operation(summary = "openapi.brand.delete.sum", description = "openapi.brand.delete.desc")
    public ResponseEntity<BrandResponse> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
