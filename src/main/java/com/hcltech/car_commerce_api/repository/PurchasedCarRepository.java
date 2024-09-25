package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.PurchasedCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedCarRepository extends JpaRepository<PurchasedCar,Integer> {
}
