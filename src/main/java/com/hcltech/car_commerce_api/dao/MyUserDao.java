package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MyUserDao {
    public MyUserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public void saveUser(MyUser myUser) {
        userRepository.save(myUser);
    }
}
