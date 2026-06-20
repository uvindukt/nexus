package com.nexus.analytics.infrastructure.web.controller;

import com.nexus.analytics.infrastructure.config.SseEmitterRegistry;
import com.nexus.analytics.infrastructure.web.constant.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping(value = ApiConstants.Analytics.BASE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
@Tag(name = ApiConstants.Analytics.TAG)
@RequiredArgsConstructor
public class ProductStockViewController {

    private final SseEmitterRegistry emitterRegistry;

    @GetMapping
    public SseEmitter streamProductStockChanges() {
        // TODO: Use auth sessions for client id when available
        return emitterRegistry.register(UUID.randomUUID().toString());
    }

}
