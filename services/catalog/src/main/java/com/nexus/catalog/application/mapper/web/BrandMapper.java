package com.nexus.catalog.application.mapper.web;

import com.nexus.catalog.application.dto.web.request.v1.BrandRequest;
import com.nexus.catalog.application.dto.web.response.v1.BrandResponse;
import com.nexus.catalog.domain.model.Brand;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BrandMapper {

    Brand toModel(BrandRequest request);

    List<Brand> toModel(List<BrandRequest> brandRequests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    void updateModel(BrandRequest request, @MappingTarget Brand brand);

    BrandResponse toResponse(Brand brand);

    List<BrandResponse> toResponse(List<Brand> brands);

}
