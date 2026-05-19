package com.nimbleways.springboilerplate.domain.products.handlers;

import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Component;

public class NormalProductHandler implements ProductHandler {

    private final ProductRepositoryPort products;
    private final NotificationPort notifications;

    public NormalProductHandler(ProductRepositoryPort products, NotificationPort notifications) {
        this.products = products;
        this.notifications = notifications;
    }

    @Override
    public ProductType supportedType() {
        return ProductType.NORMAL;
    }

    @Override
    public void handle(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            products.save(product);
            return;
        }
        if (product.getLeadTime() > 0) {
            products.save(product);
            notifications.sendDelayNotification(product.getLeadTime(), product.getName());
        }
    }
}