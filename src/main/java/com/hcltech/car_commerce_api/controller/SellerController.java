package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Cars;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carCommerceApi/v1")
public class SellerController {

    private final SellerService sellerService;


    @Autowired
    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody SellerDto sellerDto){
        return new ResponseEntity<>(sellerService.createUser(sellerDto), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Seller> getSellerByEmail(@RequestParam String email){
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String email,@RequestBody SellerDto sellerDto ) throws Exception {
        String response = sellerService.udpateSeller(email,sellerDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSeller(@RequestParam String email){
        String response = sellerService.deleteSeller(email);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/cars")
    public ResponseEntity<List<Cars>> getAllCars(){
        return ResponseEntity.ok(sellerService.getAllCars());
    }
}
