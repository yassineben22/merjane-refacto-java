package com.nimbleways.springboilerplate.domain.products.handlers;

import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeasonalProductHandler implements ProductHandler {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public SeasonalProductHandler(ProductRepository productRepository,
                                  NotificationService notificationService) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
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
            productRepository.save(product);
            return;
        }

        if (today.plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(product.getName());
            product.setAvailable(0);
            productRepository.save(product);
            return;
        }

        if (product.getSeasonStartDate().isAfter(today)) {
            notificationService.sendOutOfStockNotification(product.getName());
            productRepository.save(product);
            return;
        }

        productRepository.save(product);
        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}