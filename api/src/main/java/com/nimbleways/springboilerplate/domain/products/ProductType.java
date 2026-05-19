package com.nimbleways.springboilerplate.domain.products;

public enum ProductType {
    NORMAL, SEASONAL, EXPIRABLE;

    public static ProductType from(String raw) {
        return ProductType.valueOf(raw);
    }
}