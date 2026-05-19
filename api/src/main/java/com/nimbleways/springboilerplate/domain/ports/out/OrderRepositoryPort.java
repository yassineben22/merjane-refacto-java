package com.nimbleways.springboilerplate.domain.ports.out;

import com.nimbleways.springboilerplate.entities.Order;

import java.util.Optional;

public interface OrderRepositoryPort {
    Optional<Order> findById(Long orderId);
}