package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Integer> {
    List<Cars> findAll();

}
