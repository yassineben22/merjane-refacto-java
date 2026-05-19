package com.nimbleways.springboilerplate.infrastructure.config;

import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductHandlerRegistry;
import com.nimbleways.springboilerplate.domain.products.handlers.NormalProductHandler;
import com.nimbleways.springboilerplate.domain.products.handlers.SeasonalProductHandler;
import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductHandlersConfig {

    @Bean
    public ProductHandler normalProductHandler(ProductRepositoryPort products, NotificationPort notifications) {
        return new NormalProductHandler(products, notifications);
    }

    @Bean
    public ProductHandler seasonalProductHandler(ProductRepositoryPort products, NotificationPort notifications) {
        return new SeasonalProductHandler(products, notifications);
    }

    @Bean
    public ProductHandlerRegistry productHandlerRegistry(List<ProductHandler> handlers) {
        return new ProductHandlerRegistry(handlers);
    }
}