package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.BuyerDAO;
import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return buyerDAO.createBuyer(toBuyerEntity(buyerDTO));
    }

    public BuyerDTO getBuyerByEmail(String email){
        return toBuyerDTO(buyerDAO.getBuyerByEmail(email));
    }

    public String updateUser(String email,BuyerDTO buyerDTO) throws Exception {
        return buyerDAO.updateBuyer(email, toBuyerEntity(buyerDTO));
    }

    public BuyerDTO toBuyerDTO(Buyer buyer){
        return modelMapper.map(buyer, BuyerDTO.class);
    }

    public Buyer toBuyerEntity(BuyerDTO buyerDTO){
        return modelMapper.map(buyerDTO, Buyer.class);
    }
}
