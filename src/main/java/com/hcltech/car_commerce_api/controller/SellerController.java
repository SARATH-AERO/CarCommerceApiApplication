package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.dto.MessageDto;
import com.hcltech.car_commerce_api.dto.ResponseSellerDto;
import com.hcltech.car_commerce_api.dto.UpdateSellerDto;
import com.hcltech.car_commerce_api.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary="Get seller details by email" ,
            description = "Retrieves seller details including a list of cars using the seller's email.")
    public ResponseEntity<ResponseSellerDto> getSellerByEmail(@RequestParam String email){
        return new ResponseEntity<>(sellerService.getSellerByEmail(email), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(
            summary="Add a car to a seller" ,
            description = "Associates a car with a seller using seller's email.")
    public ResponseEntity<MessageDto> updateSellerCar(@RequestParam String email,@RequestBody CarDto carDto) {
        return new ResponseEntity<>(sellerService.updateSellerCar(email,carDto), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(
            summary="Delete a seller by email" ,
            description = "Deletes a seller from the System using their email Id.")
    public ResponseEntity<MessageDto> deleteSeller(@RequestParam String email){
        return new ResponseEntity<>(sellerService.deleteSeller(email), HttpStatus.OK);

    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(
            summary="Update seller details by email" ,
            description = "Update the details of  seller based on seller's email.")
    public ResponseEntity<MessageDto> updateUser(@RequestParam String email, @Valid @RequestBody UpdateSellerDto updateSellerDto)
    {
        return ResponseEntity.ok(sellerService.updateSeller(email,updateSellerDto));
    }

}
