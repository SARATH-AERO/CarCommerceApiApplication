package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    private BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BuyerDTO buyerDTO){
        String response = buyerService.createUser(buyerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BuyerDTO> getBuyerByEmail(@RequestParam String email){
        BuyerDTO buyerDTO = buyerService.getBuyerByEmail(email);
        return new ResponseEntity<>(buyerDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String email,@RequestBody BuyerDTO buyerDTO ) throws Exception {
        String response = buyerService.updateUser(email,buyerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
