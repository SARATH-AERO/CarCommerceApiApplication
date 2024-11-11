package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.MessageDto;
import com.hcltech.car_commerce_api.dto.ResponseSellerDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.dto.UpdateSellerDto;
import com.hcltech.car_commerce_api.entity.*;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public MessageDto addSellerCar(String email, CarDto carDto)  {

        Optional<Seller> existingSeller = sellerDao.getSellerByEmail(email);
        if(existingSeller.isEmpty())
            throw new NotFoundException(email + " seller not present");
        Seller seller = existingSeller.get();

        Car car =  carService.toCarEntity(carDto);
        seller.getCarList().add(car);
        sellerDao.createSeller(seller);
        return MessageDto.builder().message(email+" car details added successfully").build();
    }

    public MessageDto deleteSeller(String email) {
        int deletedCount =  sellerDao.deleteSeller(email);
        if(deletedCount == 0){
            throw new NotFoundException("Seller email: " + email);
        }
        myUserDao.deleteUser(email);
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

    public MessageDto updateSeller(String email, UpdateSellerDto updateSellerDto) {
        Optional<Seller> sellerOptional = sellerDao.getSellerByEmail(email);
        if (sellerOptional.isEmpty())
            throw new NotFoundException(email + " seller not present");
        Seller seller = sellerOptional.get();
        modelMapper.map(updateSellerDto, seller);
        sellerDao.createSeller(seller);
        return MessageDto.builder().message(email + " seller details updated successfully").build();
    }
    
    public MessageDto updateCar(String email, int id, CarDto carDTO) {
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if (seller.isEmpty())
            throw new NotFoundException(email + " seller not present");

        Optional<Car> car = carService.findById(id);
        if (car.isEmpty())
            throw new NotFoundException(email + " car not present");
        modelMapper.map(carDTO, car.get());
        carService.addCar(car.get());
        seller.get().getCarList().add(car.get());
        sellerDao.createSeller(seller.get());
        return MessageDto.builder().message(email + " seller car"+ id + " details updated successfully").build();
    }

}
