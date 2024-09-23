package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.repo.AuthorityRepository;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import com.hcltech.car_commerce_api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerDao {

    private final BuyerRepository buyerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BuyerDao(BuyerRepository buyerRepository, ModelMapper modelMapper, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.buyerRepository = buyerRepository;
        this.modelMapper = modelMapper;
    }

    public void createBuyer(Buyer buyer){
         buyerRepository.save(buyer);

    }

    public Optional<Buyer> getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email);
    }

    public void updateBuyer(String email, BuyerDto updateBuyerDto) throws Exception {
        Optional<Buyer> existingBuyer = buyerRepository.findByEmail(email);

        if (existingBuyer.isEmpty())
            throw new Exception(email + " buyer not present");

        Buyer modifiedBuyer = existingBuyer.get();
        modelMapper.map(updateBuyerDto, modifiedBuyer);

        // Update the modified buyer
        buyerRepository.save(modifiedBuyer);
    }

    public int deleteBuyer(String email){

        return buyerRepository.deleteByEmail(email);
    }
}
