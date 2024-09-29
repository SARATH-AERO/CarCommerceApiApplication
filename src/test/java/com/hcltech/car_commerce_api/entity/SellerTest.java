package com.hcltech.car_commerce_api.entity;

import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class SellerTest {
    
    @Mock
    private SellerRepository sellerRepository;
    @InjectMocks
    private SellerDao sellerDao;
    private Seller seller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        seller = new Seller();
        seller.setEmail("test@example.com");
    }

    @Test
    void testOnCreate() {
        seller.onCreate();
        assertNotNull(seller.getCreatedAt(), "createdAt should not be null");
        assertNotNull(seller.getModifiedDate(), "updatedAt should not be null");
        when(sellerRepository.save(seller)).thenReturn(seller);

        sellerDao.createSeller(seller);

        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    void testOnUpdate() {
        seller.onCreate();
        when(sellerRepository.save(seller)).thenReturn(seller);
        sellerDao.createSeller(seller);
        Awaitility.await().atMost(1000, TimeUnit.MICROSECONDS);
        seller.onUpdate();
        verify(sellerRepository, times(1)).save(seller);

    }
    
}
