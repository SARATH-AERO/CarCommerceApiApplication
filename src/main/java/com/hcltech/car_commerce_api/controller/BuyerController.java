package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.service.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary="Get buyer details by email" ,
            description = "Retrieves buyer details including a list of purchased cars using the buyer's email.")
    public ResponseEntity<ResponseBuyerDto> getBuyerByEmail(@RequestParam String email){
        return new ResponseEntity<>(buyerService.getBuyerByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/getAllCars")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    @Operation(
            summary="Get list of available cars to purchase by buyer email" ,
            description = "Retrieves the list of cars available for purchase.")
    public ResponseEntity<List<CarDto>> getAllCars(){
        return new ResponseEntity<>(buyerService.getAllCar(), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    @Operation(
            summary="Update buyer details by email" ,
            description = "Update the details of  buyer based on buyer's email.")
    public ResponseEntity<MessageDto> updateUser(@RequestParam String email, @Valid @RequestBody UpdateBuyerDto updateBuyerDto )
    {
        return ResponseEntity.ok(buyerService.updateBuyer(email,updateBuyerDto));
    }


    @PutMapping("/carPurchase")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    @Operation(
            summary="Purchase a car by buyer email and Car Id" ,
            description = "Assign a car to the buyer.")
    public ResponseEntity<MessageDto> purchaseCar(@RequestParam String email, @RequestParam Integer carId){
        return new ResponseEntity<>(buyerService.purchaseCar(email, carId), HttpStatus.OK);

    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    @Operation(
            summary="Delete a buyer by email" ,
            description = "Deletes a buyer from the System using their email Id.")
    public ResponseEntity<MessageDto> deleteBuyer(@RequestParam String email){
        return ResponseEntity.ok(buyerService.deleteBuyer(email));

    }

}
