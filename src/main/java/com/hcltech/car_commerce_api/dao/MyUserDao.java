package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.repository.MyUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDao {
    private final MyUserRepository myUserRepository;
    public MyUserDao(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    public Optional<MyUser> findByUsername(String email){
       return myUserRepository.findByUsername(email);
    }
    public void saveUser(MyUser myUser) {
        myUserRepository.save(myUser);
    }

    public void deleteUser(String email){
        myUserRepository.deleteByUsername(email);
    }

}
