package com.nimbleways.springboilerplate.application;

import com.nimbleways.springboilerplate.domain.ports.in.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.domain.ports.out.OrderRepositoryPort;
import com.nimbleways.springboilerplate.domain.products.ProductHandlerRegistry;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import org.springframework.stereotype.Service;

@Service
public class ProcessOrderService implements ProcessOrderUseCase {

    private final OrderRepositoryPort orders;
    private final ProductHandlerRegistry productHandlers;

    public ProcessOrderService(OrderRepositoryPort orders, ProductHandlerRegistry productHandlers) {
        this.orders = orders;
        this.productHandlers = productHandlers;
    }

    @Override
    public Long process(Long orderId) {
        Order order = orders.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order " + orderId));
        for (Product product : order.getItems()) {
            productHandlers.handle(product);
        }
        return order.getId();
    }
}