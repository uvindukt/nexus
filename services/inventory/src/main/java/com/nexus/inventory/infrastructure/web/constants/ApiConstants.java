package com.nexus.inventory.infrastructure.web.constants;

public class ApiConstants {

    private ApiConstants() {
    }

    public static final String V1 = "/v1";
    public static final String ID = "/{id}";
    public static final String Product = "/products";
    public static final String ProductId = "/{productId}";

    public static final class Stock {
        public static final String BASE = V1 + "/stock";
        public static final String TAG = "Stock Management";
        public static final String ADD = "/add";
    }

    public static final class Metadata {
        public static final String BASE = V1 + "/metadata";
        public static final String TAG = "Metadata Management";
        public static final String STOCK_STATUS = "/stock-status";
    }

}
