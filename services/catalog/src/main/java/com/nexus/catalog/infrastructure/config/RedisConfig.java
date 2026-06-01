package com.nexus.catalog.infrastructure.config;

import com.nexus.catalog.application.dto.web.response.v1.ProductResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ProductResponse> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, ProductResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        JacksonJsonRedisSerializer<ProductResponse> serializer = new JacksonJsonRedisSerializer<>(JsonMapper.builder().build(), ProductResponse.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }

}