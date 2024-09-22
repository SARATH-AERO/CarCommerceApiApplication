package com.hcltech.car_commerce_api.repo;

import com.hcltech.car_commerce_api.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}