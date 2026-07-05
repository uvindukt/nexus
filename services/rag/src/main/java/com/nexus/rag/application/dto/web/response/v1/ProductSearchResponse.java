package com.nexus.rag.application.dto.web.response.v1;

import com.nexus.rag.domain.model.ProductStockView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public record ProductSearchResponse(
        String answer,
        @Nullable
        List<ProductStockView> products
) {
}
