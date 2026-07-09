package com.nexus.rag.domain.service;

import com.nexus.rag.domain.exception.SearchPipelineException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Slf4j
@Service
public class PromptSanitizerImpl implements PromptSanitizer {

    // Defensive layer configuration constants
    private static final int MAX_QUERY_LENGTH = 400;
    private static final Pattern CONTROL_CHARACTERS = Pattern.compile("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]");
    private static final Pattern MULTI_WHITESPACE = Pattern.compile("\\s+");

    @NullMarked
    @Override
    public String sanitizeQuery(String rawQuery) {

        if (!StringUtils.hasText(rawQuery)) {
            throw new SearchPipelineException("Query text cannot be empty or null");
        }

        // Rule 1: Enforce strict length boundaries to mitigate DOS/Over-tokenization attacks
        String trimmed = rawQuery.trim();
        if (trimmed.length() > MAX_QUERY_LENGTH) {
            log.warn("Query length {} exceeded max allowed limit ({}). Truncating query.", trimmed.length(), MAX_QUERY_LENGTH);
            trimmed = trimmed.substring(0, MAX_QUERY_LENGTH).trim();
        }

        // Rule 2: Clean hidden or destructive ASCII control characters
        String cleanChars = CONTROL_CHARACTERS.matcher(trimmed).replaceAll("");

        // Rule 3: Collapse multiple continuous spaces/newlines into a single clean space
        String normalizedSpace = MULTI_WHITESPACE.matcher(cleanChars).replaceAll(" ");

        return normalizedSpace.trim();

    }

}
