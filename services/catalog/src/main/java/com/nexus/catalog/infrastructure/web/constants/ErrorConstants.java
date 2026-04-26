package com.nexus.catalog.infrastructure.web.constants;

public final class ErrorConstants {

    private ErrorConstants() {
    }

    public static final String TIMESTAMP = "timestamp";

    public static final class Title {
        public static final String CONFLICT = "Conflict";
        public static final String NOT_FOUND = "Not Found";
        public static final String INVALID_HIERARCHY = "Invalid Hierarchy";
        public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    }

    public static final class Type {
        public static final String CONFLICT = "https://nexus.com/errors/conflict";
        public static final String NOT_FOUND = "https://nexus.com/errors/not-found";
        public static final String INVALID_HIERARCHY = "https://nexus.com/errors/invalid-hierarchy";
        public static final String INTERNAL_SERVER_ERROR = "https://nexus.com/errors/internal-server-error";
    }

    public static final class Message {
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    }

    public static final class Log {
        public static final String DUPLICATE_ENTRY = "Duplicate Entry Exception: {}";
        public static final String ENTRY_NOT_FOUND = "Entry Not Found Exception: {}";
        public static final String INVALID_HIERARCHY = "Invalid Hierarchy Exception: {}";
        public static final String UNHANDLED_EXCEPTION = "Unhandled Exception: ";
    }


}
