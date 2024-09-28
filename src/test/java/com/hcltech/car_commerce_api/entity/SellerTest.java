package com.hcltech.car_commerce_api.entity;

import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SellerTest {  
    
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
    public void testOnCreate() {
        seller.onCreate();
        assertNotNull(seller.getCreatedAt(), "createdAt should not be null");
        assertNotNull(seller.getModifiedDate(), "updatedAt should not be null");
        when(sellerRepository.save(seller)).thenReturn(seller);

        sellerDao.createSeller(seller);

        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    public void testOnUpdate() throws InterruptedException {
        seller.onCreate();
        when(sellerRepository.save(seller)).thenReturn(seller);
        sellerDao.createSeller(seller);
        Thread.sleep(1000);
        seller.onUpdate();
        verify(sellerRepository, times(1)).save(seller);

    }
    
}
