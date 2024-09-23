package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
}
