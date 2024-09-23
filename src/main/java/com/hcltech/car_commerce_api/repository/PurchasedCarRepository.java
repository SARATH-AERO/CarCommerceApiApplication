package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.PurchasedCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedCarRepository extends JpaRepository<PurchasedCar,Integer> {
}
