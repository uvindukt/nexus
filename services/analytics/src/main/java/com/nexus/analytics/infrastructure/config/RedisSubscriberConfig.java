package com.nexus.analytics.infrastructure.config;

import com.nexus.analytics.application.service.AnalyticsRedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisSubscriberConfig {

    private final AnalyticsRedisSubscriber subscriber;

    @Value("${redis.channel}")
    private String redisChannel;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(subscriber, new ChannelTopic(redisChannel));
        return container;

    }

}
