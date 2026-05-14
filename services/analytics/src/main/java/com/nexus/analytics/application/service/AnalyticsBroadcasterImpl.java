package com.nexus.analytics.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class AnalyticsBroadcasterImpl implements AnalyticsBroadcaster {

    private static final String CHANNEL = "nexus:analytics:sse";
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final Sinks.Many<String> sink;

    public AnalyticsBroadcasterImpl(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        // Multicast ensures all active SSE connections get the message
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        // Trigger the listener immediately on startup
        this.initSubscription();
    }

    private void initSubscription() {
        this.redisTemplate.listenToChannel(CHANNEL)
                .map(ReactiveSubscription.Message::getMessage)
                .doOnNext(msg -> {
                    Sinks.EmitResult result = sink.tryEmitNext(msg);
                    if (result.isFailure()) {
                        log.warn("Failed to emit to SSE sink: {}", result);
                    }
                })
                .doOnError(e -> log.error("Redis subscription error: {}", e.getMessage()))
                // Retry with backoff if Redis connection drops
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(2)))
                .subscribe();
    }

    @Override
    public void broadcast(String payload) {
        this.redisTemplate.convertAndSend(CHANNEL, payload)
                .doOnError(e -> log.error("Failed to broadcast to Redis: {}", e.getMessage()))
                // Subscribe here because this is often the end of a Kafka chain, but keep it clean.
                .subscribe(count -> log.debug("Broadcasted to {} Redis subscribers", count));
    }

    @Override
    public Flux<String> getSseFlux() {
        return sink.asFlux()
                // Heartbeat to keep SSE connections alive (every 20s)
                .mergeWith(Flux.interval(Duration.ofSeconds(20)).map(i -> "keep-alive-ping"))
                .doOnCancel(() -> log.debug("Client disconnected from SSE stream"));
    }

}
