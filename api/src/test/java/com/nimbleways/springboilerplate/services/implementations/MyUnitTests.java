package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@UnitTest
public class MyUnitTests {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    //notifyDelay
    @Test
    public void notifyDelay_setsLeadTime_savesAndNotifies() {
        Product product = new Product(null, 15, 0, "NORMAL", "RJ45 Cable", null, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        productService.notifyDelay(product.getLeadTime(), product);

        assertEquals(0, product.getAvailable());
        assertEquals(15, product.getLeadTime());
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
        Mockito.verify(notificationService, Mockito.times(1))
                .sendDelayNotification(product.getLeadTime(), product.getName());
    }

    //seasonal
    @Test
    public void handleSeasonal_whenLeadTimeExceedsSeasonEnd_setsAvailableToZeroAndNotifiesOutOfStock() {
        Product product = new Product(null, 200, 0, "SEASONAL", "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(5));

        productService.handleSeasonalProduct(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendOutOfStockNotification(product.getName());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    public void handleSeasonal_whenSeasonNotStartedYet_notifiesOutOfStockAndSaves() {
        Product product = new Product(null, 5, 0, "SEASONAL", "Grapes", null,
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(90));

        productService.handleSeasonalProduct(product);

        Mockito.verify(notificationService).sendOutOfStockNotification(product.getName());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    public void handleSeasonal_inSeasonButNoStock_notifiesDelay() {
        Product product = new Product(null, 5, 0, "SEASONAL", "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(30));

        productService.handleSeasonalProduct(product);

        Mockito.verify(notificationService).sendDelayNotification(5, product.getName());
        Mockito.verify(productRepository).save(product);
    }

    //expirable
    @Test
    public void handleExpired_whenAvailableAndNotExpired_decrementsStock() {
        Product product = new Product(null, 15, 10, "EXPIRABLE", "Butter",
                LocalDate.now().plusDays(5), null, null);

        productService.handleExpiredProduct(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
        Mockito.verifyNoInteractions(notificationService);
    }

    @Test
    public void handleExpired_whenExpired_notifiesAndSetsAvailableToZero() {
        LocalDate expiry = LocalDate.now().minusDays(2);
        Product product = new Product(null, 90, 6, "EXPIRABLE", "Milk", expiry, null, null);

        productService.handleExpiredProduct(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendExpirationNotification(product.getName(), expiry);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    public void handleExpired_whenAvailableIsZero_notifiesAndSetsAvailableToZero() {
        LocalDate expiry = LocalDate.now().plusDays(10);
        Product product = new Product(null, 5, 0, "EXPIRABLE", "Yogurt", expiry, null, null);

        productService.handleExpiredProduct(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendExpirationNotification(product.getName(), expiry);
        Mockito.verify(productRepository).save(product);
    }
}