package com.nimbleways.springboilerplate.domain.products.handlers;

import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class SeasonalProductHandler implements ProductHandler {

    private final ProductRepositoryPort products;
    private final NotificationPort notifications;

    public SeasonalProductHandler(ProductRepositoryPort products, NotificationPort notifications) {
        this.products = products;
        this.notifications = notifications;
    }

    @Override
    public ProductType supportedType() {
        return ProductType.SEASONAL;
    }

    @Override
    public void handle(Product product) {
        LocalDate today = LocalDate.now();
        boolean inSeason = today.isAfter(product.getSeasonStartDate())
                && today.isBefore(product.getSeasonEndDate());

        if (inSeason && product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            products.save(product);
            return;
        }
        if (today.plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
            notifications.sendOutOfStockNotification(product.getName());
            product.setAvailable(0);
            products.save(product);
            return;
        }
        if (product.getSeasonStartDate().isAfter(today)) {
            notifications.sendOutOfStockNotification(product.getName());
            products.save(product);
            return;
        }
        products.save(product);
        notifications.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}