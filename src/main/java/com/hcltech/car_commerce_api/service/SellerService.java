package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.*;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerService {

    private final SellerDao sellerDao;
    private final ModelMapper modelMapper;

    @Autowired
    public SellerService(ModelMapper modelMapper, SellerDao sellerDao){
        this.modelMapper = modelMapper;
        this.sellerDao = sellerDao;
    }

    public  Seller getSellerByEmail(String email){
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if(seller.isEmpty())
            throw new NotFoundException("Seller email: " + email);
        return seller.get();
    }

    public void findSellerByEmail(String email) {
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if (seller.isPresent())
            throw new AlreadyExistException(email + " email address");
    }

    public String updateSeller(String email, CarDto carDto) throws Exception {
        return sellerDao.updateSeller(email, carDto );
    }

    public String deleteSeller(String email) {
        int deletedCount =  sellerDao.deleteSeller(email);
        if(deletedCount == 0){
            throw new NotFoundException("Seller email: " + email);
        }
        return email+ " seller deleted successfully";
    }

    public void createSeller(SellerDto sellerDto){
        sellerDao.createSeller(toSellerEntity(sellerDto));
    }

    public SellerDto toSellerDto(Seller seller){
        return modelMapper.map(seller, SellerDto.class);
    }

    public Seller toSellerEntity(SellerDto sellerDto){
        return modelMapper.map(sellerDto, Seller.class);
    }


    public List<Car> getAllCars() {
        return sellerDao.getAllCars();
    }

}
