package com.hcltech.car_commerce_api.entity;

import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import com.hcltech.car_commerce_api.repository.PurchasedCarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PurchasedCarTest {
    @Mock
    private PurchasedCarRepository purchasedCarRepository;
    private PurchasedCar purchasedCar;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        purchasedCar = new PurchasedCar();
        purchasedCar.setCarName("I10");
    }

    @Test
    public void testOnCreate() {
        purchasedCar.onCreate();
        assertNotNull(purchasedCar.getPurchasedDate(), "purchasedDate should not be null");
        assertTrue(purchasedCar.getPurchasedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        purchasedCarRepository.save(purchasedCar);
        verify(purchasedCarRepository, times(1)).save(purchasedCar);
    }

}
