package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarDaoTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarDao carDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGetAllCar() {
        // Given
        Car car1 = Car.builder()
                .id(1)
                .carName("Toyota")
                .brand("Corolla")
                .model("SUV")
                .manufacturerYear(1996)
                .colour("Green")
                .price(98899.50)
                .engineNumber("82jd92kjnnd9jnd9")
                .build();

        Car car2 = Car.builder()
                .id(2)
                .carName("Honda")
                .brand("Civic")
                .model("SUV")
                .manufacturerYear(1986)
                .colour("Blue")
                .price(23899.50)
                .engineNumber("97shcidjnnd9jnd9")
                .build();

        List<Car> cars = Arrays.asList(car1, car2);

        // When
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carDao.getAllCar();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        int carId = 1;
        Car car = Car.builder()
                .id(1)
                .carName("Toyota")
                .brand("Corolla")
                .model("SUV")
                .manufacturerYear(1996)
                .colour("Green")
                .price(98899.50)
                .engineNumber("82jd92kjnnd9jnd9")
                .build();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        Optional<Car> result = carDao.findById(carId);
        assertTrue(result.isPresent());
    }

    @Test
     void testFindById_NotFound() {
        int carId = 1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        Optional<Car> result = carDao.findById(carId);
        assertFalse(result.isPresent());
    }

    @Test
     void testDeleteById() {
        int carId = 1;
        doNothing().when(carRepository).deleteById(carId);
        carDao.deleteById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }
}