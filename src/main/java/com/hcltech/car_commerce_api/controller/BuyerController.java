package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
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
    public ResponseEntity<Buyer> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
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
