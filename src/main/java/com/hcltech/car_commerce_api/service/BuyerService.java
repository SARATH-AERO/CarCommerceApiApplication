package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.BuyerDAO;
import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.exception.BuyerEmailAlreadyExistsException;
import com.hcltech.car_commerce_api.exception.BuyerNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerService {

    private ModelMapper modelMapper;
    private BuyerDAO buyerDAO;

    @Autowired
    public BuyerService(ModelMapper modelMapper, BuyerDAO buyerDAO){
        this.modelMapper = modelMapper;
        this.buyerDAO = buyerDAO;
    }

    public String createUser(BuyerDTO buyerDTO){

        Optional<Buyer> buyer =  buyerDAO.getBuyerByEmail(buyerDTO.getEmail());
        if(buyer.isPresent())
            throw new BuyerEmailAlreadyExistsException(buyerDTO.getEmail() + " email address is already used");

         buyerDAO.createBuyer(toBuyerEntity(buyerDTO));
         return buyerDTO.getFirstName()+" buyer added successfully";

    }

    public Buyer getBuyerByEmail(String email){
        Optional<Buyer> buyer = buyerDAO.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new BuyerNotFoundException("Buyer not found with email: " + email);

        return buyer.get();
    }

    public String udpateBuyer(String email, BuyerDTO buyerDTO) throws Exception {

        Optional<Buyer> buyer =  buyerDAO.getBuyerByEmail(buyerDTO.getEmail());
        if(buyer.isEmpty())
            throw new BuyerNotFoundException("Buyer not found with email: " + email);

        return buyerDAO.updateBuyer(email, buyerDTO);
    }

    public String deleteBuyer(String email) {
       int deletedCount =  buyerDAO.deleteBuyer(email);
       if(deletedCount == 0){
           throw new BuyerNotFoundException("Buyer not found with email: " + email);
       }
        return email+ " buyer deleted successfully";
    }

    public BuyerDTO toBuyerDTO(Buyer buyer){
        return modelMapper.map(buyer, BuyerDTO.class);
    }

    public Buyer toBuyerEntity(BuyerDTO buyerDTO){
        return modelMapper.map(buyerDTO, Buyer.class);
    }

}
