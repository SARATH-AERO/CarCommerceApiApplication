package com.hcltech.car_commerce_api.repository;

import com.hcltech.car_commerce_api.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}