package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.MessageDto;
import com.hcltech.car_commerce_api.dto.ResponseSellerDto;
import com.hcltech.car_commerce_api.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carCommerceApi/v1/seller")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }

    @GetMapping("/email")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ResponseSellerDto> getSellerByEmail(@RequestParam String email){
        return new ResponseEntity<>(sellerService.getSellerByEmail(email), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<MessageDto> updateUser(@RequestParam String email,@Valid @RequestBody CarDto carDto) {
        return new ResponseEntity<>( sellerService.updateSeller(email,carDto), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<MessageDto> deleteSeller(@RequestParam String email){
        return new ResponseEntity<>(sellerService.deleteSeller(email), HttpStatus.OK);

    }

}
