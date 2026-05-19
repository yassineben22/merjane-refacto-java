package com.nimbleways.springboilerplate.domain.products;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductHandler {
    ProductType supportedType();

    void handle(Product product);
}