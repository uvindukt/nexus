package com.nexus.catalog.infrastructure.web.constants;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String V1 = "/v1";
    public static final String ID = "/{id}";

    public static final class Categories {
        public static final String BASE = V1 + "/categories";
        public static final String TAG = "Category Management";
    }

}
