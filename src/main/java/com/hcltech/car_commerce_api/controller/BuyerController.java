package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.PurchasedCarDto;
import com.hcltech.car_commerce_api.dto.ResponseBuyerDto;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carCommerceApi/v1/buyer")
public class BuyerController {

    private final BuyerService buyerService;


    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<ResponseBuyerDto> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/getAllCars")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<?> getAllCars(){
        return new ResponseEntity<>(buyerService.getAllCars(), HttpStatus.OK);
    }

    @PutMapping("/carPurchase")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<PurchasedCarDto> purchaseCar(@RequestParam String email, @RequestParam Integer carId){
        return new ResponseEntity<>(buyerService.purchaseCar(email, carId), HttpStatus.OK);

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<?> updateUser(@RequestParam String email,@RequestBody BuyerDto buyerDTO ) throws Exception {
        return ResponseEntity.ok(buyerService.updateBuyer(email,buyerDTO));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<?> deleteBuyer(@RequestParam String email){
        return ResponseEntity.ok(buyerService.deleteBuyer(email));

    }

}
