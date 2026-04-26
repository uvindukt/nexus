package com.nexus.catalog.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;
import com.nexus.catalog.application.dto.web.validation.Validate;
import com.nexus.catalog.application.dto.web.view.InboundView;
import com.nexus.catalog.application.dto.web.view.OutboundView;
import com.nexus.catalog.application.service.CategoryService;
import com.nexus.catalog.infrastructure.web.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.Categories.BASE)
@Tag(name = "Category Management")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.create.sum", description = "openapi.category.create.desc")
    public ResponseEntity<CategoryResponse> create(@JsonView(InboundView.Create.class) @Validated(Validate.Create.class) @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequest));
    }

    @GetMapping
    @JsonView(OutboundView.Brief.class)
    @Operation(summary = "openapi.category.findAll.sum", description = "openapi.category.findAll.desc")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.findById.sum", description = "openapi.category.findById.desc")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PutMapping(ApiConstants.ID)
    @JsonView(OutboundView.Detail.class)
    @Operation(summary = "openapi.category.update.sum", description = "openapi.category.update.desc")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @JsonView(InboundView.Update.class) @Validated(Validate.Update.class) @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryRequest));
    }

    @DeleteMapping(ApiConstants.ID)
    @Operation(summary = "openapi.category.delete.sum", description = "openapi.category.delete.desc")
    public ResponseEntity<CategoryResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
