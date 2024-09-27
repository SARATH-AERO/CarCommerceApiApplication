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

public class CarDaoTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarDao carDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCar() {
        // Given
        Car car1 = new Car(1, "Toyota", "Corolla","SUV",1996,"Green",98899.50,"82jd92kjnnd9jnd9");
        Car car2 = new Car(2, "Honda", "Civic","SUV",1986,"blue",23899.50,"97shcidjnnd9jnd9");
        List<Car> cars = Arrays.asList(car1, car2);

        // When
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carDao.getAllCar();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        // Given
        int carId = 1;
        Car car = new Car(1, "Toyota", "Corolla","SUV",1996,"Green",98899.50,"82jd92kjnnd9jnd9");

        // When
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        Optional<Car> result = carDao.findById(carId);

        // Then
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindById_NotFound() {
        // Given
        int carId = 1;

        // When
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        Optional<Car> result = carDao.findById(carId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteById() {
        // Given
        int carId = 1;

        // When
        doNothing().when(carRepository).deleteById(carId);
        carDao.deleteById(carId);

        // Then
        verify(carRepository, times(1)).deleteById(carId);
    }
}