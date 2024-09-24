package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carCommerceApi/v1/seller")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }


    @GetMapping("/email")
    public ResponseEntity<Seller> getSellerByEmail(@RequestParam String email){
        return new ResponseEntity<>(sellerService.getSellerByEmail(email), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String email,@RequestBody CarDto carDto) throws Exception {
        return new ResponseEntity<>( sellerService.updateSeller(email,carDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSeller(@RequestParam String email){
        return new ResponseEntity<>(sellerService.deleteSeller(email), HttpStatus.OK);

    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars(){
        return ResponseEntity.ok(sellerService.getAllCars());
    }
}
