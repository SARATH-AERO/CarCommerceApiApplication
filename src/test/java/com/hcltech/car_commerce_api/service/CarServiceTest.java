package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.CarDao;
import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class CarServiceTest {
    @Mock
    private CarDao carDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CarService carService;

    private Car car;
    private CarDto carDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car = Car.builder().carName("auto")
                .brand("BMW").
                model("2012").build();

        carDto = CarDto.builder().carName("auto")
                .brand("BMW").
                model("2012").build();

    }
    @Test
    void testGetAllCar() {
        when(carDao.getAllCar()).thenReturn(List.of(car));
        List<Car> result = carService.getAllCar();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        when(carDao.findById(1)).thenReturn(Optional.of(car));
        Optional<Car> byId = carService.findById(1);
        assertTrue(byId.isPresent());
        assertEquals("BMW", byId.get().getBrand());
    }

    @Test
    void testDeleteById() {
        int carId = 1;
        doNothing().when(carDao).deleteById(carId);
        carService.deleteById(carId);
        verify(carDao, times(1)).deleteById(carId);
    }


    @Test
    void testToCarDto() {
        when(modelMapper.map(car, CarDto.class)).thenReturn(carDto);
        CarDto map = carService.toCarDto(car);
        assertEquals("BMW", map.getBrand());

    }

    @Test
    void testToCarEntity() {
        when(modelMapper.map(carDto, Car.class)).thenReturn(car);
        Car carEntity = carService.toCarEntity(carDto);
        assertEquals("BMW", carEntity.getBrand());

    }




}
