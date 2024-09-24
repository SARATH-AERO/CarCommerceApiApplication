package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.security.JwtUtil;
import com.hcltech.car_commerce_api.security.UserDetailsServiceImpl;
import com.hcltech.car_commerce_api.service.UserLoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/authentication")
public class UserLoginController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserLoginService userLoginService;

    @Autowired
    public UserLoginController(AuthenticationManager authenticationManager,
                               UserDetailsServiceImpl userDetailsServiceImpl,
                               UserLoginService userLoginService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userLoginService = userLoginService;
    }


    @GetMapping("/login")
    public ResponseEntity<LoginDto> createToken(@RequestParam String username, @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        }
        catch (BadCredentialsException e){
            throw  new BadCredentialsException("Invalid username or password", e);
        }
        return ResponseEntity.ok(LoginDto.builder().message(username +"login successfully").jwtToken(
                userLoginService.generateJwt(
                        userDetailsServiceImpl.loadUserByUsername(username))).build());

    }

    @PostMapping("/seller")
    public ResponseEntity<LoginDto> createSeller(@Valid @RequestBody SellerDto sellerDto){
        return new ResponseEntity<>(userLoginService.createSeller(sellerDto,"ROLE_SELLER"), HttpStatus.CREATED);
    }

    @PostMapping("/buyer")
    public ResponseEntity<LoginDto> createBuyer(@Valid @RequestBody BuyerDto buyerDTO){
        return new ResponseEntity<>(userLoginService.createBuyer(buyerDTO,"ROLE_BUYER"), HttpStatus.CREATED);
    }

}
