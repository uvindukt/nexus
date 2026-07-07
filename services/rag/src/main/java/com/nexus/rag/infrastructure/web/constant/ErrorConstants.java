package com.nexus.rag.infrastructure.web.constant;

public final class ErrorConstants {

    private ErrorConstants() {
    }

    public static final String TIMESTAMP = "timestamp";

    public static final class Title {
        public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    }

    public static final class Type {
        public static final String INTERNAL_SERVER_ERROR = "https://nexus.com/errors/internal-server-error";
    }

    public static final class Message {
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    }

    public static final class Log {
        public static final String UNHANDLED_EXCEPTION = "Unhandled Exception: ";
    }


}
