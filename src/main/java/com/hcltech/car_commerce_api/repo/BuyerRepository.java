package com.hcltech.car_commerce_api.repo;

import com.hcltech.car_commerce_api.entity.Buyer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    Optional<Buyer> findByEmail(String email);

    @Modifying
    @Transactional
    int deleteByEmail(String email);
}
