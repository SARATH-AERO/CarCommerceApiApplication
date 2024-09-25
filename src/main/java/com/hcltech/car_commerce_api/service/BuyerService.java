package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.dao.CarDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dao.PurchasedCarDao;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.PurchasedCar;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BuyerService {

    private final ModelMapper modelMapper;
    private final CarService carService;
    private final BuyerDao buyerDao;
    private final PurchasedCarDao purchasedCarDao;
    private final MyUserDao myUserDao;


    @Autowired
    public BuyerService(ModelMapper modelMapper,
                        BuyerDao buyerDao,
                        CarService carService,
                        PurchasedCarDao purchasedCarDao ,
                        MyUserDao myUserDao){
        this.modelMapper = modelMapper;
        this.buyerDao = buyerDao;
        this.carService =carService;
        this.purchasedCarDao =purchasedCarDao;
        this.myUserDao = myUserDao;
    }
    public void findBuyerByEmail(String email) {
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if (buyer.isPresent())
            throw new AlreadyExistException(email +" buyer email address");
    }

    public void createBuyer(BuyerDto buyerDto){
        buyerDao.createBuyer(toBuyerEntity(buyerDto));
    }


    public ResponseBuyerDto getBuyerByEmail(String email){
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new NotFoundException("Buyer email: " + email);
        return toDto(buyer.get());
    }

    public MessageDto deleteBuyer(String email) {
       int deletedCount =  buyerDao.deleteBuyer(email);
        myUserDao.deleteUser(email);
       if(deletedCount == 0){
           throw new NotFoundException("Buyer email: " + email);
       }
        return MessageDto.builder().message(email+ " buyer deleted successfully").build();
    }

    public Buyer toBuyerEntity(BuyerDto buyerDTO){
        return modelMapper.map(buyerDTO, Buyer.class);
    }

    public List<CarDto> getAllCar() {
        return carService.getAllCar().stream().map(carService::toCarDto).toList();
    }

    public MessageDto purchaseCar(String email, Integer carId) {

        Optional<Car> car =  carService.findById(carId);
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);

        if(buyer.isEmpty()){
          throw new NotFoundException("email" +email);
        }
        if(car.isEmpty()){
            throw new NotFoundException("CarId " +carId);
        }
        PurchasedCar purchasedCar = modelMapper.map(car.get(),PurchasedCar.class);
        buyer.get().getPurchasedCarsList().add(purchasedCar);
        buyerDao.createBuyer(buyer.get());
        purchasedCarDao.addPurchasedCar(purchasedCar);
        carService.deleteById(carId);

        return MessageDto.builder().message(email+"has Purchased"+purchasedCar.getCarName()).build();
    }

    public ResponseBuyerDto toDto(Buyer buyer){
        return modelMapper.map(buyer, ResponseBuyerDto.class);
    }
}
