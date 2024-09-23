package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService){
        this.buyerService = buyerService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BuyerDTO buyerDTO){
        return new ResponseEntity<>(buyerService.createUser(buyerDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Buyer> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String email,@RequestBody BuyerDTO buyerDTO )
            throws Exception {
        return new ResponseEntity<>(buyerService.udpateBuyer(email,buyerDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBuyer(@RequestParam String email){
        return new ResponseEntity<>( buyerService.deleteBuyer(email), HttpStatus.OK);

    }

}
