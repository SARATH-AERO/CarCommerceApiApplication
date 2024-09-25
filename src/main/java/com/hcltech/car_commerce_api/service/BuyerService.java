package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.dao.CarDao;
import com.hcltech.car_commerce_api.dao.PurchasedCarDao;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.PurchasedCarDto;
import com.hcltech.car_commerce_api.dto.ResponseBuyerDto;
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
    private final BuyerDao buyerDao;
    private final CarDao carDao;
    private final PurchasedCarDao purchasedCarDao;

    @Autowired
    public BuyerService(ModelMapper modelMapper, BuyerDao buyerDao,CarDao carDao
            ,PurchasedCarDao purchasedCarDao){
        this.modelMapper = modelMapper;
        this.buyerDao = buyerDao;
        this.carDao = carDao;
        this.purchasedCarDao =purchasedCarDao;
    }
    public void findBuyerByEmail(String email) {
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if (buyer.isPresent())
            throw new AlreadyExistException(email +" email address");
    }

    public void createBuyer(BuyerDto buyerDto){
        buyerDao.createBuyer(toBuyerEntity(buyerDto));
    }


    public ResponseBuyerDto getBuyerByEmail(String email){
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new NotFoundException("Buyer email: " + email);
        ResponseBuyerDto responseBuyerDto = modelMapper.map(buyer.get(), ResponseBuyerDto.class);
        return responseBuyerDto;
    }

    public Map<String,String> updateBuyer(String email, BuyerDto buyerDTO) throws Exception {

        Optional<Buyer> buyer =  buyerDao.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new NotFoundException("Buyer email: " + email);
        buyerDao.updateBuyer(email, buyerDTO);
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "success");
        responseJson.put("message", email+" buyer details updated successfully");
        return responseJson;
    }

    public Map<String, String> deleteBuyer(String email) {
       int deletedCount =  buyerDao.deleteBuyer(email);
       if(deletedCount == 0){
           throw new NotFoundException("Buyer email: " + email);
       }

        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "success");
        responseJson.put("message", email+ " buyer deleted successfully");
        return responseJson;
    }

    public Buyer toBuyerEntity(BuyerDto buyerDTO){
        return modelMapper.map(buyerDTO, Buyer.class);
    }

    public List<Car> getAllCars() {
        return carDao.getAllCars();
    }

    public PurchasedCarDto purchaseCar(String email, Integer carId) {

        Optional<Car> car =  carDao.findById(carId);
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);

        if(buyer.isEmpty()){
          throw new NotFoundException("email" +email);
        }
        if(car.isEmpty()){
            throw new NotFoundException("CarId" +carId);
        }
        PurchasedCar purchasedCar = modelMapper.map(car.get(),PurchasedCar.class);
        buyer.get().getPurchasedCarsList().add(purchasedCar);
        buyerDao.createBuyer(buyer.get());
        purchasedCarDao.addPurchasedCar(purchasedCar);
        carDao.deleteById(carId);
        return  modelMapper.map(purchasedCar,PurchasedCarDto.class);
    }
}
