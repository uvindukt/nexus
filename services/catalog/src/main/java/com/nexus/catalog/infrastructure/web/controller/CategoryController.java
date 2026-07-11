package com.nexus.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.InboundView;
import com.nexus.catalog.application.dto.web.OutboundView;
import com.nexus.catalog.application.dto.web.Validate;
import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;
import com.nexus.catalog.application.dto.web.response.v1.GenericBatchOperationResponse;
import com.nexus.catalog.application.service.CategoryService;
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
@RequestMapping(value = ApiConstants.Categories.BASE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = ApiConstants.Categories.TAG)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.create.sum", description = "openapi.category.create.desc")
    public ResponseEntity<CategoryResponse> create(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryRequest));
    }

    @PostMapping(ApiConstants.BATCH)
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.category.createBatch.sum", description = "openapi.category.createBatch.desc")
    public ResponseEntity<GenericBatchOperationResponse> createBatch(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody List<CategoryRequest> categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createBatch(categoryRequest));
    }

    @GetMapping
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.category.findAll.sum", description = "openapi.category.findAll.desc")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.findById.sum", description = "openapi.category.findById.desc")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PutMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.update.sum", description = "openapi.category.update.desc")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequest));
    }

    @DeleteMapping(ApiConstants.ID)
    @Operation(summary = "openapi.category.delete.sum", description = "openapi.category.delete.desc")
    public ResponseEntity<CategoryResponse> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
