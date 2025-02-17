package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.CarDao;
import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.ResponseCarDto;
import com.hcltech.car_commerce_api.entity.Car;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarDao carDao;
    private final ModelMapper modelMapper;

    public CarService(CarDao carDao,ModelMapper modelMapper){
        this.carDao = carDao;
        this.modelMapper = modelMapper;
    }

    public List<Car> getAllCar() {
        return carDao.getAllCar();
    }
    public Optional<Car> findById(Integer carId){
        return carDao.findById(carId);
    }
    public void deleteById (Integer carId){
        carDao.deleteById(carId);
    }

    public CarDto toCarDto(Car car){
        return modelMapper.map(car, CarDto.class);
    }

    public ResponseCarDto toResponseCarDto(Car car){
        return modelMapper.map(car, ResponseCarDto.class);
    }

    public Car toCarEntity(CarDto cardto){
        return modelMapper.map(cardto, Car.class);
    }


    public void addCar(Car car){
        carDao.addCar(car);
    }


}
