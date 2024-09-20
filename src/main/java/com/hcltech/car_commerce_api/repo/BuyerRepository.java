package com.hcltech.car_commerce_api.repo;

import com.hcltech.car_commerce_api.entity.Buyer;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    Buyer findByEmail(String email);
}
