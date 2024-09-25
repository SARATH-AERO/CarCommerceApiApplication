package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.CarDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.MessageDto;
import com.hcltech.car_commerce_api.dto.ResponseSellerDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.*;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;;

@Service
public class SellerService {

    private final SellerDao sellerDao;
    private final ModelMapper modelMapper;
    private final MyUserDao myUserDao;
    private final CarService carService;

    @Autowired
    public SellerService(ModelMapper modelMapper,
                         SellerDao sellerDao,
                         CarService carService,
                         MyUserDao myUserDao){
        this.modelMapper = modelMapper;
        this.sellerDao = sellerDao;
        this.carService = carService;
        this.myUserDao = myUserDao;
    }

    public ResponseSellerDto getSellerByEmail(String email){
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if(seller.isEmpty())
            throw new NotFoundException("Seller email: " + email);
        return toSellerDto(seller.get());
    }

    public void findSellerByEmail(String email) {
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if (seller.isPresent())
            throw new AlreadyExistException(email + " seller email address");
    }
    @Transactional
    public MessageDto updateSeller(String email, CarDto carDto){
        Optional<Seller> existingSeller = sellerDao.getSellerByEmail(email);
        if(existingSeller.isEmpty())
            throw new NotFoundException(email + " seller not present");
        Seller seller = existingSeller.get();
        seller.getCarList().add(carService.toCarEntity(carDto));
        sellerDao.createSeller(seller);
        return MessageDto.builder().message(email+" car details added successfully").build();
    }

    public MessageDto deleteSeller(String email) {
        int deletedCount =  sellerDao.deleteSeller(email);
        myUserDao.deleteUser(email);
        if(deletedCount == 0){
            throw new NotFoundException("Seller email: " + email);
        }
        return  MessageDto.builder().message(email+ " seller deleted successfully").build();
    }

    public void createSeller(SellerDto sellerDto){
        sellerDao.createSeller(toSellerEntity(sellerDto));
    }

    public ResponseSellerDto toSellerDto(Seller seller){
        return modelMapper.map(seller, ResponseSellerDto.class);
    }
    public Seller toSellerEntity(SellerDto sellerDto){
        return modelMapper.map(sellerDto, Seller.class);
    }

}
