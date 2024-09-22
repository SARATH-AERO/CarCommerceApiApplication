package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Users;
import com.hcltech.car_commerce_api.repo.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public void saveUser(Users user) {
        userRepository.save(user);
    }
}
