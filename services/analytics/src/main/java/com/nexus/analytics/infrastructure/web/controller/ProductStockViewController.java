package com.nexus.analytics.infrastructure.web.controller;

import com.nexus.analytics.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.analytics.application.service.ProductStockViewService;
import com.nexus.analytics.infrastructure.web.constants.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = ApiConstants.Analytics.BASE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
@Tag(name = ApiConstants.Analytics.TAG)
@RequiredArgsConstructor
public class ProductStockViewController {

    private final ProductStockViewService productStockViewService;

    @GetMapping
    public Flux<ProductStockViewResponse> streamProductStockChanges() {
        return productStockViewService.streamActiveStockChanges();
    }

}
