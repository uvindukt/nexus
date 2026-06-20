package com.nexus.analytics.infrastructure.web.constant;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String V1 = "/v1";
    public static final String ID = "/{id}";

    public static final class Analytics {
        public static final String BASE = V1 + "/analytics";
        public static final String TAG = "Analytics Management";
    }

    public static final class Products {
        public static final String BASE = V1 + "/products";
        public static final String TAG = "Product Management";
    }

    public static final class Metadata {
        public static final String BASE = V1 + "/metadata";
        public static final String TAG = "Metadata Management";
        public static final String PRODUCT_STATUS = "/product-status";
    }

}
