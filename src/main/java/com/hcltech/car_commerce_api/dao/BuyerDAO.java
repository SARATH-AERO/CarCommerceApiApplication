package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.repo.AuthorityRepository;
import com.hcltech.car_commerce_api.repo.BuyerRepository;
import com.hcltech.car_commerce_api.repo.UserRepository;
import com.hcltech.car_commerce_api.repository.BuyerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerDAO {

    private final BuyerRepository buyerRepository;
    private final ModelMapper modelMapper;

    public BuyerDAO(BuyerRepository buyerRepository, ModelMapper modelMapper) {
        this.buyerRepository = buyerRepository;
        this.modelMapper = modelMapper;
    }

    public void createBuyer(Buyer buyer){
         buyerRepository.save(buyer);

    }

    public Optional<Buyer> getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email);
    }

    public void updateBuyer(String email,BuyerDTO updateBuyerDTO) throws Exception {
        Optional<Buyer> existingBuyer = buyerRepository.findByEmail(email);

        if(existingBuyer.isEmpty())
            throw new Exception(email + " buyer not present");
        Buyer modifiedBuyer = existingBuyer.get();
        modelMapper.map(updateBuyerDTO, modifiedBuyer);
        buyerRepository.save(modifiedBuyer);
    }

    public int deleteBuyer(String email){
        return buyerRepository.deleteByEmail(email);
    }
}
