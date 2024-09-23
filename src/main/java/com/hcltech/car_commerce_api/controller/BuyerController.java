package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.service.BuyerService;
import com.hcltech.car_commerce_api.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    private final BuyerService buyerService;


    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @PostMapping
    public ResponseEntity<?> createBuyer(@RequestBody BuyerDto buyerDTO){
        return new ResponseEntity<>(buyerService.createBuyer(buyerDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Buyer> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/getAllCars")
    public ResponseEntity<?> getAllCars(){
        return new ResponseEntity<>(buyerService.getAllCars(), HttpStatus.OK);
    }

    @PutMapping("/carPurchase")
    public ResponseEntity<?> purchaseCar(@RequestParam String email, @RequestParam int carId){
        return new ResponseEntity<>(buyerService.purchaseCar(email, carId), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestParam String email,@RequestBody BuyerDto buyerDTO ) throws Exception {
        return ResponseEntity.ok(buyerService.updateBuyer(email,buyerDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBuyer(@RequestParam String email){
        return ResponseEntity.ok(buyerService.deleteBuyer(email));

    }

}
