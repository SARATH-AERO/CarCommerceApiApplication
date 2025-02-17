package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Authority;
import com.hcltech.car_commerce_api.repository.AuthorityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorityDao {
    private final AuthorityRepository authorityRepository;

    public AuthorityDao(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public void saveAuthority(Authority authority) {
        authorityRepository.save(authority);
    }
}
