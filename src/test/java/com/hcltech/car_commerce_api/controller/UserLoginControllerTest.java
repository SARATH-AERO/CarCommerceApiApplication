package com.hcltech.car_commerce_api.controller;

import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.security.UserDetailsServiceImpl;
import com.hcltech.car_commerce_api.service.UserLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserLoginService userLoginService;

    @InjectMocks
    private UserLoginController userLoginController;

    private SellerDto sellerDto;
    private BuyerDto buyerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerDto = SellerDto.builder()
                .email("seller@example.com")
                .firstName("Seller")
                .lastName("User")
                .password("password123")
                .build();

        buyerDto = BuyerDto.builder()
                .email("buyer@example.com")
                .firstName("Buyer")
                .lastName("User")
                .password("password123")
                .build();
    }

    @Test
    void testCreateToken_Success() {
        String username = "user@example.com";
        String password = "password123";
        LoginDto expectedLoginDto = LoginDto.builder()
                .message(username + "login successfully")
                .jwtToken("mockJwtToken")
                .build();

        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(userDetailsMock);

        when(userLoginService.generateJwt(any())).thenReturn("mockJwtToken");

        doThrow(new BadCredentialsException("Invalid username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        ResponseEntity<LoginDto> responseEntity = userLoginController.createToken(username, password);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedLoginDto.getMessage(), responseEntity.getBody().getMessage());
        assertEquals(expectedLoginDto.getJwtToken(), responseEntity.getBody().getJwtToken());
    }


    @Test
    void testCreateToken_InvalidCredentials() {
        String username = "user@example.com";
        String password = "wrongPassword";

        doThrow(new BadCredentialsException("Invalid username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> userLoginController.createToken(username, password));
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void testCreateSeller_Success() {
        LoginDto expectedLoginDto = LoginDto.builder()
                .message("seller@example.com added successfully")
                .jwtToken("mockJwtToken")
                .build();

        when(userLoginService.createSeller(any(SellerDto.class), anyString())).thenReturn(expectedLoginDto);

        ResponseEntity<LoginDto> responseEntity = userLoginController.createSeller(sellerDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedLoginDto.getMessage(), Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(expectedLoginDto.getJwtToken(), responseEntity.getBody().getJwtToken());
    }

    @Test
    void testCreateBuyer_Success() {
        LoginDto expectedLoginDto = LoginDto.builder()
                .message("buyer@example.com added successfully")
                .jwtToken("mockJwtToken")
                .build();

        when(userLoginService.createBuyer(any(BuyerDto.class), anyString())).thenReturn(expectedLoginDto);

        ResponseEntity<LoginDto> responseEntity = userLoginController.createBuyer(buyerDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedLoginDto.getMessage(), Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(expectedLoginDto.getJwtToken(), responseEntity.getBody().getJwtToken());
    }
}
