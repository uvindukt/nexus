package com.nexus.analytics.infrastructure.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Module r2dbcJsonModule() {
        SimpleModule module = new SimpleModule();
        // Register a custom serializer for the R2DBC Json class
        module.addSerializer(Json.class, new JsonSerializer<>() {
            @Override
            public void serialize(Json value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // value.asString() returns the raw JSON string (e.g., {"id": 1})
                // gen.writeRawValue ensures Jackson doesn't escape the quotes
                gen.writeRawValue(value.asString());
            }
        });
        return module;
    }
}