package com.hcltech.car_commerce_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.dto.BuyerDTO;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.exception.BuyerNotFoundException;
import com.hcltech.car_commerce_api.service.BuyerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuyerService buyerService;

    @Test
    @DisplayName("")
    @Rollback(value = false)
    void createBuyer_shouldReturnOkStatus() throws Exception {
        // Arrange
        BuyerDTO buyerDTO = BuyerDTO.builder()
                .email("test@gmai.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholing nalloor")
                .city("chennai")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();
        String expectedResponse = buyerDTO.getFirstName()+" buyer added successfully";

        when(buyerService.createBuyer(any(BuyerDTO.class))).thenReturn(expectedResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(buyerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    @DisplayName("")
    void getBuyerByEmail_shouldReturnBuyer() throws Exception {
        // Arrange
        String email = "test@example.com";
        Buyer buyer = Buyer.builder()
                .email("test@example.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholinga nalloor")
                .city("chennai")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();

        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/buyer").param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
        // Verify returned JSON data

    }

    @Test
    void updateUser_shouldReturnOkStatus() throws Exception {
        // Arrange
        String email = "test@example.com";
        BuyerDTO buyerDTO = BuyerDTO.builder()
                .email("test@example.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholinga nalloor")
                .city("chennai")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();
        String expectedResponse = buyerDTO.getEmail()+" buyer details added successfully";;

        when(buyerService.udpateBuyer(eq(email), any(BuyerDTO.class))).thenReturn(expectedResponse);

        // Act and Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(buyerDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    void deleteBuyer_shouldReturnOkStatus() throws Exception {
        // Arrange
        String email = "test@example.com";
        String expectedResponse = email+ " buyer deleted successfully";

        when(buyerService.deleteBuyer(email)).thenReturn(expectedResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/buyer")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    void createBuyer_shouldReturnBadRequestForInvalidInput() throws Exception {
        // List of test cases for invalid inputs
        List<BuyerDTO> invalidBuyerDTOs = Arrays.asList(
                new BuyerDTO(null, "John", "Doe", "1234567890", "Address", "City", "600001", "License"), // Invalid email
                new BuyerDTO("invalid-email", "John", "Doe", "1234567890", "Address", "City", "600001", "License"), // Invalid email format
                new BuyerDTO("test@example.com", null, "Doe", "1234567890", "Address", "City", "600001", "License"), // Null first name
                new BuyerDTO("test@example.com", "John", null, "1234567890", "Address", "City", "600001", "License"), // Null last name
                new BuyerDTO("test@example.com", "John", "Doe", null, "Address", "City", "600001", "License"), // Null phone number
                new BuyerDTO("test@example.com", "John", "Doe", "12345", "Address", "City", "600001", "License"), // Invalid phone number
                new BuyerDTO("test@example.com", "John", "Doe", "1234567890", null, "City", "600001", "License"), // Null address
                new BuyerDTO("test@example.com", "John", "Doe", "1234567890", "Address", null, "600001", "License"), // Null city
                new BuyerDTO("test@example.com", "John", "Doe", "1234567890", "Address", "City", null, "License"), // Null postal code
                new BuyerDTO("test@example.com", "John", "Doe", "1234567890", "Address", "City", "12345", "License"), // Invalid postal code
                new BuyerDTO("test@example.com", "John", "Doe", "1234567890", "Address", "City", "600001", null) // Null license number
        );

        for (BuyerDTO invalidBuyerDTO : invalidBuyerDTOs) {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/buyer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invalidBuyerDTO)))
                    .andExpect(status().isBadRequest());
        }

    }

    @Test
    void getBuyerByEmail_shouldReturnNotFoundWhenExceptionThrown() throws Exception {
        // Arrange
        String email = "unknown@example.com";
        String expectedResponse = "Buyer not found with email: " + email;

        when(buyerService.getBuyerByEmail(email)).thenThrow(new BuyerNotFoundException("Buyer not found with email: " + email));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/buyer")
                        .param("email", email))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedResponse));


    }



}
