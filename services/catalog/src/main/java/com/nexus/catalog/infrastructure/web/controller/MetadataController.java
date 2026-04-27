package com.nexus.catalog.infrastructure.web.controller;

import com.nexus.catalog.domain.model.ProductStatus;
import com.nexus.catalog.infrastructure.web.constants.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(ApiConstants.Metadata.BASE)
@Tag(name = ApiConstants.Metadata.TAG)
@RestController
public class MetadataController {

    @GetMapping(ApiConstants.Metadata.PRODUCT_STATUS)
    public ResponseEntity<List<String>> getProductStatus() {
        return ResponseEntity.ok(Arrays.stream(ProductStatus.values()).map(Enum::name).collect(Collectors.toList()));
    }

}
