package com.hcltech.car_commerce_api.dao.service;

import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerDao {
    @Autowired
    SellerRepository sellerRepository;


    public Optional<Seller> getById(Integer id)
    {
        return sellerRepository.findById(id);
    }
    public Seller create(Seller seller)
    {
        return sellerRepository.save(seller);
    }

//    public List<Seller> getAll() {
//        return sellerRepository.findAll();
//    }
}
