package com.nimbleways.springboilerplate.domain.ports.in;

public interface ProcessOrderUseCase {
    Long process(Long orderId);
}