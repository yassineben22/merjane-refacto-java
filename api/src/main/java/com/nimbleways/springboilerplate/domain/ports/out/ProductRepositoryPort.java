package com.nimbleways.springboilerplate.domain.ports.out;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductRepositoryPort {
    Product save(Product product);
}