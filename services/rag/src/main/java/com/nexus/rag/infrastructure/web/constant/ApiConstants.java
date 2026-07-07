package com.nexus.rag.infrastructure.web.constant;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String V1 = "/v1";
    public static final String ID = "/{id}";

    public static final class RAG {
        public static final String BASE = V1 + "/rag";
        public static final String TAG = "RAG";
    }

    public static final class Metadata {
        public static final String BASE = V1 + "/metadata";
        public static final String TAG = "Metadata Management";
        public static final String PRODUCT_STATUS = "/product-status";
    }

}
