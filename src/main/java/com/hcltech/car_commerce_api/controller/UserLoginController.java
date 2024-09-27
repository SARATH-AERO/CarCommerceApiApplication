package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.security.UserDetailsServiceImpl;
import com.hcltech.car_commerce_api.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/authentication/v1/userLogin")
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

    @Operation(
            summary="Login for seller or buyer" ,
            description = "Authenticates a seller or buyer using their email and password, and return a JWT token upon successful login.")
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

    @Operation(
            summary="Register as a seller" ,
            description = "Registers a new seller in the system. Upon successful registration, a JWT token will be returned for authentication.")
    @PostMapping("/seller")
    public ResponseEntity<LoginDto> createSeller(@Valid @RequestBody SellerDto sellerDto){
        return new ResponseEntity<>(userLoginService.createSeller(sellerDto,"ROLE_SELLER"), HttpStatus.CREATED);
    }

    @Operation(
            summary="Register as a Buyer" ,
            description = "Registers a new buyer in the system. Upon successful registration, a JWT token will be returned for authentication.")
    @PostMapping("/buyer")
    public ResponseEntity<LoginDto> createBuyer(@Valid @RequestBody BuyerDto buyerDTO){
        return new ResponseEntity<>(userLoginService.createBuyer(buyerDTO,"ROLE_BUYER"), HttpStatus.CREATED);
    }

}
