package com.hcltech.car_commerce_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.configuration.SecurityConfiguration;
import com.hcltech.car_commerce_api.configuration.TestSecurityConfig;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
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
        BuyerDto buyerDto =
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", "1234567890123456");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("status", "success");
        expectedResponse.put("message", buyerDto.getFirstName()+" buyer added successfully");

       // when(buyerService.createBuyer(any(BuyerDto.class))).thenReturn(expectedResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(buyerDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(buyerDto.getFirstName()+" buyer added successfully"));


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
        BuyerDto buyerDto = BuyerDto.builder()
                .email("test@example.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholinga nalloor")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();
        String expectedResponse = buyerDto.getEmail()+" buyer details added successfully";

        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "success");
        responseJson.put("message", email+" buyer details updated successfully");

        //when(buyerService.updateBuyer(eq(email), any(BuyerDto.class))).thenReturn(responseJson);

        // Act and Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(buyerDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(buyerDto.getEmail()+" buyer details updated successfully"));


    }

    @Test
    void deleteBuyer_shouldReturnOkStatus() throws Exception {
        // Arrange
        String email = "test@example.com";
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "success");
        responseJson.put("message", email+ " buyer deleted successfully");

       // when(buyerService.deleteBuyer(email)).thenReturn(responseJson);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/buyer")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(email+" buyer deleted successfully"));


    }

    @Test
    void createBuyer_shouldReturnBadRequestForInvalidInput() throws Exception {
        List<BuyerDto> invalidBuyerDtos = Arrays.asList(
                // Valid

                // Invalid email cases
                new BuyerDto(null, "password", "John", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Null email
                new BuyerDto("invalid-email", "password", "John", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Invalid email format

                // Invalid first name
                new BuyerDto("test@example.com","password", null, "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Null first name
                new BuyerDto("test@example.com","password", "", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Empty first name

                // Invalid last name
                new BuyerDto("test@example.com","password", "John", null, "1234567890", "Address", "600001", "1234567890123456"), // Null last name
                new BuyerDto("test@example.com","password", "John", "", "1234567890", "Address", "600001", "1234567890123456"), // Empty last name

                // Invalid phone number
                new BuyerDto("test@example.com", "password", "John", "Doe", null, "Address", "600001", "1234567890123456"), // Null phone number
                new BuyerDto("test@example.com", "password", "John", "Doe", "12345", "Address", "600001", "1234567890123456"), // Invalid phone number
                new BuyerDto("test@example.com", "password", "John", "Doe", "12345678901", "Address", "600001", "1234567890123456"), // Too long phone number

                // Invalid address
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", null, "600001", "1234567890123456"), // Null address
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "", "600001", "1234567890123456"), // Empty address

                // Invalid postal code
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", null, "1234567890123456"), // Null postal code
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "12345", "1234567890123456"), // Invalid postal code
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "1234567", "1234567890123456"), // Too long postal code

                // Invalid license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", null), // Null license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", ""), // Empty license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", "12345678901234567"), // Too long license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", "12345678") // Too short license number
        );

        for (BuyerDto invalidBuyerDto : invalidBuyerDtos) {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/buyer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invalidBuyerDto)))
                    .andExpect(status().isBadRequest());
        }
    }



    @Test
    void getBuyerByEmail_shouldReturnNotFoundWhenExceptionThrown() throws Exception {
        // Arrange
        String email = "unknown@example.com";
        String expectedResponse = "Buyer not found with email: " + email;

        when(buyerService.getBuyerByEmail(email)).thenThrow(new NotFoundException("Buyer not found with email: " + email));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/buyer")
                        .param("email", email))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedResponse));


    }



}
