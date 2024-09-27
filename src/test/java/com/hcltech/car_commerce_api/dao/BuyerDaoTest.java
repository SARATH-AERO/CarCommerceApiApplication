package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuyerDaoTest {

    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private BuyerDao buyerDao;

    private Buyer buyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyer = new Buyer();
        buyer.setEmail("test@example.com");
        buyer.setFirstName("John");
        buyer.setLastName("Doe");
    }

    @Test
    void testCreateBuyer() {
        // Act
        buyerDao.createBuyer(buyer);

        // Assert
        verify(buyerRepository, times(1)).save(buyer);
    }

    @Test
    void testGetBuyerByEmail_Success() {
        // Arrange
        when(buyerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(buyer));

        // Act
        Optional<Buyer> result = buyerDao.getBuyerByEmail("test@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(buyerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testGetBuyerByEmail_NotFound() {
        // Arrange
        when(buyerRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<Buyer> result = buyerDao.getBuyerByEmail("unknown@example.com");

        // Assert
        assertFalse(result.isPresent());
        verify(buyerRepository, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testDeleteBuyer() {
        // Arrange
        when(buyerRepository.deleteByEmail("test@example.com")).thenReturn(1);

        // Act
        int result = buyerDao.deleteBuyer("test@example.com");

        // Assert
        assertEquals(1, result);
        verify(buyerRepository, times(1)).deleteByEmail("test@example.com");
    }
}
