package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerDao {

    private final BuyerRepository buyerRepository;
    @Autowired
    public BuyerDao(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public void createBuyer(Buyer buyer){
         buyerRepository.save(buyer);

    }

    public Optional<Buyer> getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email);
    }


    public int deleteBuyer(String email){
        return buyerRepository.deleteByEmail(email);
    }
}
