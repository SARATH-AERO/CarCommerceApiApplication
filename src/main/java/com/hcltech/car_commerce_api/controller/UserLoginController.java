package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.security.JwtUtil;
import com.hcltech.car_commerce_api.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class UserLoginController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserLoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


    @GetMapping("/authenticate")
    public ResponseEntity<?> createToken(@RequestParam String username, @RequestParam String password) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        }catch (BadCredentialsException e){
            throw  new BadCredentialsException("Invalid username or password", e);
        }

        // user details only fetched to provide token not verifying it.
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        final String jwtToken = jwtUtil.generateToken(userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);

    }
}
