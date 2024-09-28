package com.hcltech.car_commerce_api.entity;

import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuyerTest{
    @Mock
    private BuyerRepository buyerRepository;
    @InjectMocks
    private BuyerDao buyerDao;
    private Buyer buyer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        buyer = new Buyer();
        buyer.setEmail("test@example.com");
    }

    @Test
    public void testOnCreate() {
        buyer.onCreate();
        assertNotNull(buyer.getCreatedAt(), "createdAt should not be null");
        assertNotNull(buyer.getUpdatedAt(), "updatedAt should not be null");
        assertTrue(buyer.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(buyer.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        when(buyerRepository.save(buyer)).thenReturn(buyer);

        buyerDao.createBuyer(buyer);

        verify(buyerRepository, times(1)).save(buyer);
    }

    @Test
    public void testOnUpdate() throws InterruptedException {
        buyer.onCreate();
        when(buyerRepository.save(buyer)).thenReturn(buyer);
        buyerDao.createBuyer(buyer);
        LocalDateTime initialUpdatedAt = buyer.getUpdatedAt();
        Thread.sleep(1000);
        buyer.onUpdate();
        assertTrue(buyer.getUpdatedAt().isAfter(initialUpdatedAt), "updatedAt should be updated");
        verify(buyerRepository, times(1)).save(buyer);

    }
}
