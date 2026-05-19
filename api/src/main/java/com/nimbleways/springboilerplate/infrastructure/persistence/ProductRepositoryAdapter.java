package com.nimbleways.springboilerplate.infrastructure.persistence;

import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository delegate;

    public ProductRepositoryAdapter(ProductRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Product save(Product product) {
        return delegate.save(product);
    }
}