package com.nexus.rag.domain.service;

public interface PromptSanitizer {

    /**
     * Sanitizes the raw user query by applying defensive filtering rules.
     *
     * @param rawQuery The raw query string to sanitize.
     * @return The sanitized query string.
     */
    String sanitizeQuery(String rawQuery);

}
