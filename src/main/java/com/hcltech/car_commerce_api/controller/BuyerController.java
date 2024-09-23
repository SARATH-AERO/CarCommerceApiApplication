package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @PostMapping
    public ResponseEntity<?> createBuyer(@Valid @RequestBody BuyerDTO buyerDTO){
        return new ResponseEntity<>(buyerService.createBuyer(buyerDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Buyer> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestParam String email,@RequestBody BuyerDTO buyerDTO ) throws Exception {
        return ResponseEntity.ok(buyerService.updateBuyer(email,buyerDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBuyer(@RequestParam String email){
        return ResponseEntity.ok(buyerService.deleteBuyer(email));

    }

}
