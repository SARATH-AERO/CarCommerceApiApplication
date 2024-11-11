package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

class SellerServiceTest {

    @Mock
    private SellerDao sellerDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MyUserDao myUserDao;

    @Mock
    private CarService carService;

    @InjectMocks
    private SellerService sellerService;
    private Seller seller;
    private SellerDto sellerDto;
    private ResponseSellerDto responseSellerDto;
    private Car car;
    private CarDto carDto;
    public UpdateSellerDto updateSellerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        seller = new Seller();
        seller.setFirstName("Jane");
        seller.setLastName("Doe");
        seller.setEmail("jane.doe@example.com");
        seller.setCarList(new ArrayList<>());

        sellerDto = new SellerDto();
        sellerDto.setFirstName("John");
        sellerDto.setLastName("Doe");

        responseSellerDto = new ResponseSellerDto();
        responseSellerDto.setFirstName("John");
        responseSellerDto.setLastName("Doe");

        car = new Car();
        car.setCarName("Tesla");

        carDto = new CarDto();
        carDto.setCarName("Tesla");

        updateSellerDto   = new UpdateSellerDto();
        updateSellerDto.setFirstName("sai");

    }

    @Test
    void testGetSellerByEmail_Success() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
        when(modelMapper.map(seller, ResponseSellerDto.class)).thenReturn(responseSellerDto);

        ResponseSellerDto result = sellerService.getSellerByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testGetSellerByEmail_NotFound() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sellerService.getSellerByEmail("notfound@example.com"));
    }

    @Test
    void testFindSellerByEmail_AlreadyExists() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
         assertThrows(AlreadyExistException.class, () ->
                sellerService.findSellerByEmail("jane.doe@example.com"));
    }

    @Test
    void testFindSellerByEmail() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> sellerService.findSellerByEmail("john.doe@example.com"));

        verify(sellerDao, times(1)).getSellerByEmail("john.doe@example.com");

    }

    @Test
    void testCreateSeller_Success() {
        when(modelMapper.map(sellerDto, Seller.class)).thenReturn(seller);
        sellerService.createSeller(sellerDto);
        verify(sellerDao, times(1)).createSeller(any(Seller.class));
    }

    @Test
    void testAddSellerCar_Success() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
        when(carService.toCarEntity(carDto)).thenReturn(car);

        MessageDto result = sellerService.addSellerCar("john.doe@example.com", carDto);

        assertEquals("john.doe@example.com car details added successfully", result.getMessage());
        verify(sellerDao, times(1)).createSeller(any(Seller.class));
    }

    @Test
    void testAddSellerCar_NotFound() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sellerService.addSellerCar("notfound@example.com", carDto));
    }


    @Test
    void testUpdateSeller_Success() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
        when(modelMapper.map(seller, UpdateSellerDto.class)).thenReturn(updateSellerDto);
        MessageDto result = sellerService.updateSeller("john.doe@example.com", updateSellerDto);
        assertEquals("john.doe@example.com seller details updated successfully", result.getMessage());
        verify(sellerDao, times(1)).createSeller(any(Seller.class));
    }

    @Test
    void testUpdateSeller_NotFound() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sellerService.
                updateSeller("notfound@example.com",updateSellerDto));
    }

    @Test
    void testDeleteSeller_Success() {
        when(sellerDao.deleteSeller(anyString())).thenReturn(1);
        MessageDto result = sellerService.deleteSeller("jane.doe@example.com");
        assertEquals("jane.doe@example.com seller deleted successfully", result.getMessage());
        verify(myUserDao, times(1)).deleteUser(anyString());
    }

    @Test
    void testDeleteSeller_NotFound() {
        when(sellerDao.deleteSeller(anyString())).thenReturn(0);

        assertThrows(NotFoundException.class, () -> sellerService.deleteSeller("notfound@example.com"));
    }

    @Test
    void testUpdateCar_Success() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
        doNothing().when(sellerDao).createSeller(any(Seller.class)); // Mocking createSeller to do nothing

        when(carService.findById(anyInt())).thenReturn(Optional.of(car));

        MessageDto result = sellerService.updateCar("john.doe@example.com", 1, carDto);

        assertEquals("john.doe@example.com seller car1 details updated successfully", result.getMessage());
        verify(carService, times(1)).addCar(car);
        verify(sellerDao, times(1)).createSeller(seller);
    }



    @Test
    void testUpdateCar_SellerNotFound() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sellerService.updateCar("notfound@example.com", 1, carDto));
    }

    @Test
    void testUpdateCar_CarNotFound() {
        when(sellerDao.getSellerByEmail(anyString())).thenReturn(Optional.of(seller));
        when(carService.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sellerService.updateCar("john.doe@example.com", 1, carDto));
    }
}
