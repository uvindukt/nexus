package com.nexus.analytics.application.service;

import com.nexus.analytics.application.dto.web.event.v1.SseEnvelope;

public interface AnalyticsBroadcasterService {

    void publish(SseEnvelope envelope);

}
