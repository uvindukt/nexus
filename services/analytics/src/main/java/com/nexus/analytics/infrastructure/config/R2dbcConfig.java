package com.nexus.analytics.infrastructure.config;

import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Custom R2DBC Configuration for the Nexus Analytics Service.
 * Provides the ConnectionFactory and registers JSONB converters.
 */
@Configuration
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    // Inject the auto-configured ConnectionFactory
    private final ConnectionFactory connectionFactory;

    @Override
    @NonNull
    public ConnectionFactory connectionFactory() {
        return this.connectionFactory;
    }

    /**
     * Registers custom converters used by Spring Data R2DBC.
     * These converters ensure that the 'payload' column in the 'inbox'
     * table is treated as a native JSONB object.
     */
    @Override
    @NonNull
    protected List<Object> getCustomConverters() {
        return List.of(
                new JsonToPostgresWritingConverter(),
                new JsonToPostgresReadingConverter()
        );
    }

    /**
     * Forces the R2DBC driver to bind the {@link Json} parameter
     * as a PostgreSQL-compatible type during INSERT/UPDATE operations.
     */
    @WritingConverter
    public static class JsonToPostgresWritingConverter implements Converter<Json, Json> {
        @Override
        public Json convert(@NonNull Json source) {
            return source;
        }
    }

    /**
     * Ensures that data read from a JSONB column is correctly
     * mapped back into the {@link Json} wrapper in the Java entity.
     */
    @ReadingConverter
    public static class JsonToPostgresReadingConverter implements Converter<Json, Json> {
        @Override
        public Json convert(@NonNull Json source) {
            return source;
        }
    }
}