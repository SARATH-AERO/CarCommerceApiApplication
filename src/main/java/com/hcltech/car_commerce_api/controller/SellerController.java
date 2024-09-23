package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carCommerceApi/v1")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    //    @GetMapping
//    public ResponseEntity<List<SellerDto>> getAll()
//    {
//     final List<SellerDto> result= sellerService.getAll();
//     return ResponseEntity.ok(result);
//    }
//
    @GetMapping("/{id}")
    public ResponseEntity<SellerDto> getById(@PathVariable Integer id)
    {
        final Optional<SellerDto> result= sellerService.getById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<SellerDto> create(@RequestBody SellerDto sellerDto) {
        return ResponseEntity.ok(sellerService.create(sellerDto));
    }
}
