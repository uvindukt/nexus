package com.nexus.analytics.application.service;

import reactor.core.publisher.Flux;

public interface AnalyticsBroadcaster {

    public void broadcast(String payload);

    public Flux<String> getSseFlux();

}
