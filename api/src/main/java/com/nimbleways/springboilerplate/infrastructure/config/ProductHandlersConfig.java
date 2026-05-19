package com.nimbleways.springboilerplate.infrastructure.config;

import com.nimbleways.springboilerplate.domain.products.ProductHandler;
import com.nimbleways.springboilerplate.domain.products.ProductHandlerRegistry;
import com.nimbleways.springboilerplate.domain.products.handlers.ExpirableProductHandler;
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
    public ProductHandler normalProductHandler(ProductRepositoryPort p, NotificationPort n) {
        return new NormalProductHandler(p, n);
    }

    @Bean
    public ProductHandler seasonalProductHandler(ProductRepositoryPort p, NotificationPort n) {
        return new SeasonalProductHandler(p, n);
    }

    @Bean
    public ProductHandler expirableProductHandler(ProductRepositoryPort p, NotificationPort n) {
        return new ExpirableProductHandler(p, n);
    }

    @Bean
    public ProductHandlerRegistry productHandlerRegistry(List<ProductHandler> handlers) {
        return new ProductHandlerRegistry(handlers);
    }
}