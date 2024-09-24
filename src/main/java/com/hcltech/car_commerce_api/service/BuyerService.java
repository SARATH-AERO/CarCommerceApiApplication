package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.AuthorityDao;
import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BuyerService {

    private final ModelMapper modelMapper;
    private final BuyerDao buyerDao;

    @Autowired
    public BuyerService(ModelMapper modelMapper, BuyerDao buyerDao){
        this.modelMapper = modelMapper;
        this.buyerDao = buyerDao;
    }
    public void findBuyerByEmail(String email) {
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if (buyer.isPresent())
            throw new AlreadyExistException(email +"email address");
    }

    public void createBuyer(BuyerDto buyerDto){
        buyerDao.createBuyer(toBuyerEntity(buyerDto));
    }


    public Buyer getBuyerByEmail(String email){
        Optional<Buyer> buyer = buyerDao.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new NotFoundException("Buyer email: " + email);

        return buyer.get();
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

    public List<Cars> getAllCars() {
        return carDao.getAllCars();
    }

    public Map<String, Object> purchaseCar(String email, int carId) {

        Optional<Cars> car =  carDao.findById(carId);
        Optional<Buyer> buyer = buyerDAO.getBuyerByEmail(email);

        if(buyer.isEmpty()){
            // buyer not found exception here
        }

        if(car.isEmpty()){
            // car not found exception thrown here.
        }


        PurchasedCar purchasedCar = modelMapper.map(car.get(),PurchasedCar.class);
        buyer.get().getPurchasedCarsList().add(purchasedCar);
        buyerDAO.createBuyer(buyer.get());
        purchasedCarDao.addPurchasedCar( purchasedCar);
        carDao.deleteById(carId);

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("status", "success");
        responseJson.put("message", String.format("Car ID: %d with %s purchased successfully", carId, car.get().getCarName()));
        responseJson.put("data", Map.of(
                "carId", carId,
                "carName", car.get().getCarName(),
                "purchaseDate", LocalDateTime.now().toString(),
                "paymentStatus", "Completed"
        ));

        return responseJson;

    }
}
