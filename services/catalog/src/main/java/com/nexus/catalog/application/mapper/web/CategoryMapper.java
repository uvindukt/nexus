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
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    String TO_BRIEF_RESPONSE = "toBriefResponse";

    Category toModel(CategoryRequest request);

    List<Category> toModel(List<CategoryRequest> request);

    // Updates the existing category object in its current place
    @Mapping(target = "id", ignore = true)
    void updateModel(CategoryRequest request, @MappingTarget Category exsitingCategory);

    @Mapping(target = "parent", qualifiedByName = TO_BRIEF_RESPONSE)
    CategoryResponse toResponse(Category category);

    // Maps parent category recursively without sub categories
    @Named(TO_BRIEF_RESPONSE)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "parent", qualifiedByName = TO_BRIEF_RESPONSE)
    CategoryResponse toBriefResponse(Category category);

    List<CategoryResponse> toResponse(List<Category> categories);

}
