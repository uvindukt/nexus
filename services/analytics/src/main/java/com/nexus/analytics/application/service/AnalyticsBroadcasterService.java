package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.event.v1.ProductStockViewEvent;

public interface AnalyticsBroadcasterService {

    void publish(ProductStockViewEvent event);

}
