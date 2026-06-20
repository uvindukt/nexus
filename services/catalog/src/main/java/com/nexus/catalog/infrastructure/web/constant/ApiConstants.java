package com.nexus.catalog.infrastructure.web.constant;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String V1 = "/v1";
    public static final String ID = "/{id}";

    public static final class Categories {
        public static final String BASE = V1 + "/categories";
        public static final String TAG = "Category Management";
    }

    public static final class Brands {
        public static final String BASE = V1 + "/brands";
        public static final String TAG = "Brand Management";
    }

    public static final class ProductAttributes {
        public static final String BASE = V1 + "/product-attributes";
        public static final String TAG = "Product Attribute Management";
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
