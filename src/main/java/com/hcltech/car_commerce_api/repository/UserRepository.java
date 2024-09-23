package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<MyUser, UUID> {

    Optional<MyUser> findByUsername(String username);
}



