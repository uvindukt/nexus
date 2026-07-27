package com.nexus.gateway.infrastructure.web.constant;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String FALLBACK_URL = "/fallback";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    public static final class Catalog {
        public static final String BASE = "/catalog";
        public static final String TAG = "Catalog Service";
        public static final String ERROR = "catalog-service unavailable";
        public static final String MESSAGE = "Please retry shortly.";
    }

    public static final class Inventory {
        public static final String BASE = "/inventory";
        public static final String TAG = "Inventory Service";
        public static final String ERROR = "inventory-service unavailable";
    }

    public static final class Analytics {
        public static final String BASE = "/analytics";
        public static final String TAG = "Analytics Service";
        public static final String ERROR = "analytics-service unavailable";
    }

    public static final class Rag {
        public static final String BASE = "/rag";
        public static final String TAG = "RAG Service";
        public static final String ERROR = "nexus-rag unavailable";
        public static final String MESSAGE = "AI search is temporarily down; try keyword search instead.";
    }

}
