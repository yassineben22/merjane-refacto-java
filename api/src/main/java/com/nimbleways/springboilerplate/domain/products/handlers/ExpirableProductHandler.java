package com.nimbleways.springboilerplate.domain.products.handlers;

import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductType;
import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.entities.Product;

import java.time.LocalDate;

public class ExpirableProductHandler implements ProductHandler {

    private final ProductRepositoryPort products;
    private final NotificationPort notifications;

    public ExpirableProductHandler(ProductRepositoryPort products, NotificationPort notifications) {
        this.products = products;
        this.notifications = notifications;
    }

    @Override
    public ProductType supportedType() {
        return ProductType.EXPIRABLE;
    }

    @Override
    public void handle(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            products.save(product);
            return;
        }
        notifications.sendExpirationNotification(product.getName(), product.getExpiryDate());
        product.setAvailable(0);
        products.save(product);
    }
}
