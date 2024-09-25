package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.repository.CarRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CarDao {

    private final CarRepository carRepository;

    public CarDao(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    public Optional<Car> findById(int carId) {
        return carRepository.findById(carId);
    }

    public void deleteById(int carId){
        carRepository.deleteById(carId);
    }
}
