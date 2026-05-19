package com.nimbleways.springboilerplate.infrastructure.notification;

import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NotificationAdapter implements NotificationPort {

    private final NotificationService delegate;

    public NotificationAdapter(NotificationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendDelayNotification(int leadTime, String productName) {
        delegate.sendDelayNotification(leadTime, productName);
    }

    @Override
    public void sendOutOfStockNotification(String productName) {
        delegate.sendOutOfStockNotification(productName);
    }

    @Override
    public void sendExpirationNotification(String productName, LocalDate expiryDate) {
        delegate.sendExpirationNotification(productName, expiryDate);
    }
}