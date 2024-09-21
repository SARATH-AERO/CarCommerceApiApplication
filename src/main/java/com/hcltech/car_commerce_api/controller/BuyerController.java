package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    private BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @PostMapping
    public ResponseEntity<String> createBuyer(@Valid @RequestBody BuyerDTO buyerDTO){
        String response = buyerService.createBuyer(buyerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Buyer> getBuyerByEmail(@RequestParam String email){
        Buyer buyer = buyerService.getBuyerByEmail(email);
        return new ResponseEntity<>(buyer, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String email,@RequestBody BuyerDTO buyerDTO ) throws Exception {
        String response = buyerService.udpateBuyer(email,buyerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBuyer(@RequestParam String email){
        String response = buyerService.deleteBuyer(email);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
