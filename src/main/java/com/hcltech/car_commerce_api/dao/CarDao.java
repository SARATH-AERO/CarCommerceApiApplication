package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Cars;
import com.hcltech.car_commerce_api.repository.CarsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarDao {

    private final CarsRepository carsRepository;

    public CarDao(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    public List<Cars> getAllCars(){
        return carsRepository.findAll();
    }

    public Optional<Cars> findById(int carId) {
        return carsRepository.findById(carId);
    }

    public void deleteById(int carId){
        carsRepository.deleteById(carId);
    }
}
