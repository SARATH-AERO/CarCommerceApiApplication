package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.security.JwtUtil;
import com.hcltech.car_commerce_api.security.UserDetailsServiceImpl;
import com.hcltech.car_commerce_api.service.UserLoginService;
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

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserLoginService userLoginService;

    @Autowired
    public UserLoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                               UserDetailsServiceImpl userDetailsServiceImpl,
                               UserLoginService userLoginService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userLoginService = userLoginService;
    }


    @GetMapping("/login")
    public ResponseEntity<?> createToken(@RequestParam String username, @RequestParam String password) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        }
        catch (BadCredentialsException e){
            throw  new BadCredentialsException("Invalid username or password", e);
        }
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtUtil.generateToken(userDetailsServiceImpl.loadUserByUsername(username)));
        return ResponseEntity.ok(response);

    }

    @PostMapping("/seller")
    public ResponseEntity<LoginDto> createSeller(@RequestBody SellerDto sellerDto){
        return new ResponseEntity<>(userLoginService.createSeller(sellerDto,"ROLE_SELLER"), HttpStatus.CREATED);
    }

    @PostMapping("/buyer")
    public ResponseEntity<LoginDto> createBuyer(@RequestBody BuyerDto buyerDTO){
        return new ResponseEntity<>(userLoginService.createBuyer(buyerDTO,"ROLE_BUYER"), HttpStatus.CREATED);
    }

}
