package com.nimbleways.springboilerplate.infrastructure.persistence;

import com.nimbleways.springboilerplate.domain.ports.out.OrderRepositoryPort;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderRepository delegate;

    public OrderRepositoryAdapter(OrderRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return delegate.findById(orderId);
    }
}