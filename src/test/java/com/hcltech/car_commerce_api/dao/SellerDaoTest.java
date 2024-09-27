package com.hcltech.car_commerce_api.dao;


import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SellerDaoTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerDao sellerDao;

    private Seller seller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seller = new Seller();
        seller.setId(1);
        seller.setFirstName("John");
        seller.setLastName("Doe");
        seller.setEmail("john.doe@example.com");
        seller.setPhoneNumber("1234567890");
        // Set other fields if needed
    }

    @Test
    void testCreateSeller() {
        // Arrange
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        // Act
        sellerDao.createSeller(seller);

        // Assert
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    void testGetSellerByEmail_Success() {
        // Arrange
        when(sellerRepository.findByEmail(anyString())).thenReturn(Optional.of(seller));

        // Act
        Optional<Seller> result = sellerDao.getSellerByEmail("john.doe@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(seller.getEmail(), result.get().getEmail());
        verify(sellerRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testGetSellerByEmail_NotFound() {
        // Arrange
        when(sellerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Seller> result = sellerDao.getSellerByEmail("notfound@example.com");

        // Assert
        assertFalse(result.isPresent());
        verify(sellerRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void testDeleteSeller() {
        // Arrange
        when(sellerRepository.deleteByEmail(anyString())).thenReturn(1);

        // Act
        int result = sellerDao.deleteSeller("john.doe@example.com");

        // Assert
        assertEquals(1, result);
        verify(sellerRepository, times(1)).deleteByEmail("john.doe@example.com");
    }
}