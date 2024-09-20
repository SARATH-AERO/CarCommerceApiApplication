package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.repo.BuyerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerDAO {

    private BuyerRepository buyerRepository;
    private ModelMapper modelMapper;

    public BuyerDAO(BuyerRepository buyerRepository, ModelMapper modelMapper) {
        this.buyerRepository = buyerRepository;
        this.modelMapper = modelMapper;
    }

    public String createBuyer(Buyer buyer){
        buyerRepository.save(buyer);
        return buyer.getFirstName() + " buyer added successfully";
    }

    public Buyer getBuyerByEmail(String email) {
        Optional<Buyer> buyer = Optional.ofNullable(buyerRepository.findByEmail(email));
        return buyer.get();
    }

    public String updateBuyer(String email,Buyer updateBuyer) throws Exception {
        Optional<Buyer> extisitingBuyer = Optional.ofNullable(buyerRepository.findByEmail(email));

        if(extisitingBuyer.isEmpty())
            throw new Exception(email + " buyer not present");
        Buyer modifiedBuyer = extisitingBuyer.get();
        modelMapper.map(updateBuyer, modifiedBuyer);
        buyerRepository.save(modifiedBuyer);
        return modifiedBuyer.getEmail()+" buyer details added successfully";
    }
}
