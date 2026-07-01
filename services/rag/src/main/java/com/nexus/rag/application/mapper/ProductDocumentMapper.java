package com.nexus.rag.application.mapper;

import com.nexus.rag.domain.model.ProductStockView;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.ai.document.Document;

import java.util.HashMap;
import java.util.Map;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ProductDocumentMapper {

    public Document toDocument(ProductStockView product, String contentHash) {
        return new Document(
                buildContent(product),
                buildMetadata(product, contentHash)
        );
    }

    protected String buildContent(ProductStockView product) {
        // Control what goes into the vector
        return String.format(
                "%s. %s. %s",
                product.getBrandName(),
                product.getCategoryName(),
                product.getDescription()
        );
    }

    protected Map<String, Object> buildMetadata(ProductStockView product, String contentHash) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("productId", product.getProductId().toString());
        metadata.put("brand", product.getBrandName());
        metadata.put("category", product.getCategoryName());
        metadata.put("contentHash", contentHash);
        return metadata;
    }

}