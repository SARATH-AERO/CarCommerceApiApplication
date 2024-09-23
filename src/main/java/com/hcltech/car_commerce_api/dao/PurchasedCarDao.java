package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.PurchasedCar;
import com.hcltech.car_commerce_api.repository.PurchasedCarRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PurchasedCarDao {

    private final PurchasedCarRepository purchasedCarRepository;

    public PurchasedCarDao(PurchasedCarRepository purchasedCarRepository) {
        this.purchasedCarRepository = purchasedCarRepository;
    }

    public void addPurchasedCar(PurchasedCar purchasedCar){
        purchasedCarRepository.save(purchasedCar);
    }
}
