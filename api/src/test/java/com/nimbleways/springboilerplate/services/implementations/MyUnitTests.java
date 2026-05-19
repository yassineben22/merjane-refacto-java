package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.domain.ports.out.NotificationPort;
import com.nimbleways.springboilerplate.domain.ports.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.domain.products.handlers.ExpirableProductHandler;
import com.nimbleways.springboilerplate.domain.products.handlers.NormalProductHandler;
import com.nimbleways.springboilerplate.domain.products.handlers.SeasonalProductHandler;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
public class MyUnitTests {

    @Mock private NotificationPort notifications;
    @Mock private ProductRepositoryPort products;

    private NormalProductHandler normal;
    private SeasonalProductHandler seasonal;
    private ExpirableProductHandler expirable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        normal = new NormalProductHandler(products, notifications);
        seasonal = new SeasonalProductHandler(products, notifications);
        expirable = new ExpirableProductHandler(products, notifications);
    }

    // NORMAL
    @Test public void normal_inStock_decrements() {
        Product p = new Product(null, 15, 3, "NORMAL", "RJ45 Cable", null, null, null);
        normal.handle(p);
        assertEquals(2, p.getAvailable());
        Mockito.verify(products).save(p);
        Mockito.verifyNoInteractions(notifications);
    }

    @Test public void normal_outOfStock_notifiesDelay() {
        Product p = new Product(null, 15, 0, "NORMAL", "RJ45 Cable", null, null, null);
        normal.handle(p);
        Mockito.verify(products).save(p);
        Mockito.verify(notifications).sendDelayNotification(15, "RJ45 Cable");
    }

    @Test public void normal_outOfStockNoLeadTime_doesNothing() {
        Product p = new Product(null, 0, 0, "NORMAL", "RJ45 Cable", null, null, null);
        normal.handle(p);
        Mockito.verifyNoInteractions(products);
        Mockito.verifyNoInteractions(notifications);
    }

    // SEASONAL
    @Test public void seasonal_inSeasonWithStock_decrements() {
        Product p = new Product(null, 5, 10, "SEASONAL", "Watermelon", null,
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(30));
        seasonal.handle(p);
        assertEquals(9, p.getAvailable());
        Mockito.verify(products).save(p);
    }

    @Test public void seasonal_leadTimeExceedsEnd_zeroAndNotify() {
        Product p = new Product(null, 200, 0, "SEASONAL", "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(5));
        seasonal.handle(p);
        assertEquals(0, p.getAvailable());
        Mockito.verify(notifications).sendOutOfStockNotification("Watermelon");
        Mockito.verify(products).save(p);
    }

    @Test public void seasonal_beforeStart_notifyOutOfStock() {
        Product p = new Product(null, 5, 0, "SEASONAL", "Grapes", null,
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(90));
        seasonal.handle(p);
        Mockito.verify(notifications).sendOutOfStockNotification("Grapes");
        Mockito.verify(products).save(p);
    }

    @Test public void seasonal_inSeasonNoStock_notifyDelay() {
        Product p = new Product(null, 5, 0, "SEASONAL", "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(30));
        seasonal.handle(p);
        Mockito.verify(notifications).sendDelayNotification(5, "Watermelon");
        Mockito.verify(products).save(p);
    }

    // EXPIRABLE
    @Test public void expirable_valid_decrements() {
        Product p = new Product(null, 15, 10, "EXPIRABLE", "Butter",
                LocalDate.now().plusDays(5), null, null);
        expirable.handle(p);
        assertEquals(9, p.getAvailable());
        Mockito.verify(products).save(p);
        Mockito.verifyNoInteractions(notifications);
    }

    @Test public void expirable_expired_notifyAndZero() {
        LocalDate expiry = LocalDate.now().minusDays(2);
        Product p = new Product(null, 90, 6, "EXPIRABLE", "Milk", expiry, null, null);
        expirable.handle(p);
        assertEquals(0, p.getAvailable());
        Mockito.verify(notifications).sendExpirationNotification("Milk", expiry);
        Mockito.verify(products).save(p);
    }

    @Test public void expirable_outOfStock_notifyAndZero() {
        LocalDate expiry = LocalDate.now().plusDays(10);
        Product p = new Product(null, 5, 0, "EXPIRABLE", "Yogurt", expiry, null, null);
        expirable.handle(p);
        assertEquals(0, p.getAvailable());
        Mockito.verify(notifications).sendExpirationNotification("Yogurt", expiry);
        Mockito.verify(products).save(p);
    }
}