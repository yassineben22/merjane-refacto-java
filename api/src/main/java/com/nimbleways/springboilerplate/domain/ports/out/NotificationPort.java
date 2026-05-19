package com.nimbleways.springboilerplate.domain.ports.out;

import java.time.LocalDate;

public interface NotificationPort {
    void sendDelayNotification(int leadTime, String productName);

    void sendOutOfStockNotification(String productName);

    void sendExpirationNotification(String productName, LocalDate expiryDate);
}