package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CarDto>> getAllCars(){
        return new ResponseEntity<>(buyerService.getAllCar(), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<MessageDto> updateUser(@RequestParam String email, @Valid @RequestBody UpdateBuyerDto updateBuyerDto ) throws Exception {
        return ResponseEntity.ok(buyerService.updateBuyer(email,updateBuyerDto));
    }


    @PutMapping("/carPurchase")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<MessageDto> purchaseCar(@RequestParam String email, @RequestParam Integer carId){
        return new ResponseEntity<>(buyerService.purchaseCar(email, carId), HttpStatus.OK);

    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<MessageDto> deleteBuyer(@RequestParam String email){
        return ResponseEntity.ok(buyerService.deleteBuyer(email));

    }

}
