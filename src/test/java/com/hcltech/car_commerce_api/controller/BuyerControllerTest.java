package com.hcltech.car_commerce_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

class BuyerControllerTest {

    @Mock
    private BuyerService buyerService;

    @InjectMocks
    private BuyerController buyerController;

    private MockMvc mockMvc;
    private ResponseBuyerDto responseBuyerDto;
    private MessageDto messageDto;
    private UpdateBuyerDto updateBuyerDto;
    private String email;
    private Integer carId;

    private static final ObjectMapper mapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(buyerController).build();

        email = "john.doe@example.com";
        carId = 1;

        responseBuyerDto = new ResponseBuyerDto();
        responseBuyerDto.setFirstName("John");
        responseBuyerDto.setLastName("Doe");
        responseBuyerDto.setEmail(email);
        responseBuyerDto.setAddress("New York");
        responseBuyerDto.setPhoneNumber("9876543210");
        responseBuyerDto.setPostalCode("10001");

        updateBuyerDto = new UpdateBuyerDto();
        updateBuyerDto.setFirstName("John Updated");
        updateBuyerDto.setLastName("Doe Updated");

        messageDto = new MessageDto("Success");
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void testGetBuyerByEmail_Success() throws Exception {
        when(buyerService.getBuyerByEmail(email)).thenReturn(responseBuyerDto);

        mockMvc.perform(get("/api/carCommerceApi/v1/buyer")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(buyerService, times(1)).getBuyerByEmail(email);
    }



    @Test
    @WithMockUser(roles = "BUYER")
    void testGetAllCars_Success() throws Exception {
        List<CarDto> carDtoList = Collections.emptyList(); // Adjust with actual data if needed
        when(buyerService.getAllCar()).thenReturn(carDtoList);

        mockMvc.perform(get("/api/carCommerceApi/v1/buyer/getAllCars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(buyerService, times(1)).getAllCar();
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void testPurchaseCar_Success() throws Exception {
        when(buyerService.purchaseCar(email, carId)).thenReturn(messageDto);

        mockMvc.perform(put("/api/carCommerceApi/v1/buyer/carPurchase")
                        .param("email", email)
                        .param("carId", carId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Success"));

        verify(buyerService, times(1)).purchaseCar(email, carId);
    }



    @Test
    @WithMockUser(roles = "BUYER")
    void testDeleteBuyer_Success() throws Exception {
        when(buyerService.deleteBuyer(email)).thenReturn(messageDto);

        mockMvc.perform(delete("/api/carCommerceApi/v1/buyer")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Success"));

        verify(buyerService, times(1)).deleteBuyer(email);
    }
    @Test
    @WithMockUser(roles = "BUYER")
    void testGetAllCars_EmptyList() throws Exception {
        when(buyerService.getAllCar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/carCommerceApi/v1/buyer/getAllCars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(buyerService, times(1)).getAllCar();
    }



    @Test
    @WithMockUser(roles = "BUYER")
    void testPurchaseCar_WithoutCarId() throws Exception {
        mockMvc.perform(put("/api/carCommerceApi/v1/buyer/carPurchase")
                        .param("email", email))
                .andExpect(status().isBadRequest());

        verify(buyerService, never()).purchaseCar(anyString(), anyInt());
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void testDeleteBuyer_WithoutEmail() throws Exception {
        mockMvc.perform(delete("/api/carCommerceApi/v1/buyer"))
                .andExpect(status().isBadRequest());

        verify(buyerService, never()).deleteBuyer(anyString());
    }

    @Test
    @WithMockUser(roles =  "BUYER")
    void testUpdateUser()  throws Exception{
        when(buyerService.updateBuyer(eq(email),
                any(UpdateBuyerDto.class))).thenReturn(messageDto);

        mockMvc.perform(put("/api/carCommerceApi/v1/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBuyerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(buyerService, times(1)).updateBuyer(eq(email),
                any(UpdateBuyerDto.class));
    }


}
