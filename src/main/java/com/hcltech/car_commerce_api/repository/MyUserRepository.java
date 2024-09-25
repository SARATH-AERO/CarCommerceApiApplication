package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.MyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, UUID> {

    Optional<MyUser> findByUsername(String username);

    @Modifying
    @Transactional
    void deleteByUsername(String email);
}



