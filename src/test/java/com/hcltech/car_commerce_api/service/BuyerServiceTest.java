package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.dao.PurchasedCarDao;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.PurchasedCar;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

class BuyerServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BuyerDao buyerDao;

    @Mock
    private CarService carService;

    @Mock
    private PurchasedCarDao purchasedCarDao;

    @InjectMocks
    private BuyerService buyerService;

    private Buyer buyer;
    private BuyerDto buyerDto;
    private ResponseBuyerDto responseBuyerDto;
    private Car car;
    private PurchasedCar purchasedCar;
    private UpdateBuyerDto updateBuyerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        buyer = new Buyer();
        buyer.setFirstName("John");
        buyer.setLastName("Doe");
        buyer.setEmail("john.doe@example.com");
        buyer.setPurchasedCarsList(new ArrayList<>());

        car = new Car();
        car.setId(1);
        car.setCarName("Toyota");

        purchasedCar = new PurchasedCar();
        purchasedCar.setCarName("Toyota");

        buyerDto = new BuyerDto();
        buyerDto.setFirstName("John");
        buyerDto.setLastName("Doe");

        responseBuyerDto = new ResponseBuyerDto();
        responseBuyerDto.setFirstName("John");
        responseBuyerDto.setLastName("Doe");

        updateBuyerDto = UpdateBuyerDto.builder().lastName("john").build();


    }

    @Test
    void testFindBuyerByEmail_AlreadyExists() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.of(buyer));
        assertThrows(AlreadyExistException.class, () -> buyerService.findBuyerByEmail("john.doe@example.com"));
    }

    @Test
    void testCreateBuyer_Success() {
        when(modelMapper.map(buyerDto, Buyer.class)).thenReturn(buyer);
        buyerService.createBuyer(buyerDto);
        verify(buyerDao, times(1)).createBuyer(any(Buyer.class));
    }

    @Test
    void testGetBuyerByEmail_Success() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(modelMapper.map(buyer, ResponseBuyerDto.class)).thenReturn(responseBuyerDto);

        ResponseBuyerDto result = buyerService.getBuyerByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testGetBuyerByEmail_NotFound() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> buyerService.getBuyerByEmail("nonexistent@example.com"));
    }

    @Test
    void testDeleteBuyer_NotFound() {
        when(buyerDao.deleteBuyer(anyString())).thenReturn(0);
        assertThrows(NotFoundException.class, () -> buyerService.deleteBuyer("nonexistent@example.com"));
    }

    @Test
    void testGetAllCar_Success() {
        List<Car> cars = List.of(car);
        when(carService.getAllCar()).thenReturn(cars);
        List<CarDto> carDtos = buyerService.getAllCar();
        assertEquals(1, carDtos.size());
    }


    @Test
    void testPurchaseCar_BuyerNotFound() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> buyerService.purchaseCar("nonexistent@example.com", 1));
    }

    @Test
    void testPurchaseCar_CarNotFound() {
        when(carService.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> buyerService.purchaseCar("john.doe@example.com", 1));
    }

    @Test
    void testUpdateBuyer_Success() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(modelMapper.map(any(UpdateBuyerDto.class), eq(Buyer.class))).thenAnswer(invocation -> {
            buyer.setFirstName(updateBuyerDto.getFirstName());
            return buyer;
        });
        MessageDto result = buyerService.updateBuyer("john.doe@example.com", updateBuyerDto);
        assertEquals("john.doe@example.com buyer details updated successfully", result.getMessage());
        assertEquals("John", buyer.getFirstName());
    }

    @Test
    void testUpdateBuyer_NotFound() {
        when(buyerDao.getBuyerByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> buyerService.updateBuyer("nonexistent@example.com",updateBuyerDto));
    }
}
