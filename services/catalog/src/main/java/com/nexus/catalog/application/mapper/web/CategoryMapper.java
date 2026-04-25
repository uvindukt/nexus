package com.nexus.catalog.application.mapper.web;

import com.nexus.catalog.application.dto.web.request.v1.CategoryRequest;
import com.nexus.catalog.application.dto.web.response.v1.CategoryResponse;
import com.nexus.catalog.domain.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface CategoryMapper {

    String TO_BRIEF_RESPONSE = "toBriefResponse";

    Category toModel(CategoryRequest request);

    @Mapping(target = "parent", qualifiedByName = TO_BRIEF_RESPONSE)
    CategoryResponse toResponse(Category category);

    // Maps without subCategories for parent recursively
    @Named(TO_BRIEF_RESPONSE)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "parent", qualifiedByName = TO_BRIEF_RESPONSE)
    CategoryResponse toBriefResponse(Category category);

    List<CategoryResponse> toResponses(List<Category> categories);

}
