package com.hcltech.car_commerce_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.service.BuyerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
        BuyerDto buyerDTO = BuyerDto.builder()
                .email("test@gmai.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholing nalloor")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();
        String expectedResponse = buyerDTO.getFirstName()+" buyer added successfully";

       // when(buyerService.createBuyer(any(BuyerDto.class))).thenReturn(expectedResponse);

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
        BuyerDto buyerDTO = BuyerDto.builder()
                .email("test@example.com")
                .firstName("sarath")
                .lastName("sekar")
                .address("village road, sholinga nalloor")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("hdft5569dd")
                .build();
        String expectedResponse = buyerDTO.getEmail()+" buyer details added successfully";

        when(buyerService.updateBuyer(eq(email), any(BuyerDto.class))).thenReturn(expectedResponse);

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
        List<BuyerDto> invalidBuyerDto = Arrays.asList(
                new BuyerDto(null, "John", "Doe", "1234567890", "Address", "City", "600001", "License"), // Invalid email
                new BuyerDto("invalid-email", "John", "Doe", "1234567890", "Address", "City", "600001", "License"), // Invalid email format
                new BuyerDto("test@example.com", null, "Doe", "1234567890", "Address", "City", "600001", "License"), // Null first name
                new BuyerDto("test@example.com", "John", null, "1234567890", "Address", "City", "600001", "License"), // Null last name
                new BuyerDto("test@example.com", "John", "Doe", null, "Address", "City", "600001", "License"), // Null phone number
                new BuyerDto("test@example.com", "John", "Doe", "12345", "Address", "City", "600001", "License"), // Invalid phone number
                new BuyerDto("test@example.com", "John", "Doe", "1234567890", null, "City", "600001", "License"), // Null address
                new BuyerDto("test@example.com", "John", "Doe", "1234567890", "Address", null, "600001", "License"), // Null city
                new BuyerDto("test@example.com", "John", "Doe", "1234567890", "Address", "City", null, "License"), // Null postal code
                new BuyerDto("test@example.com", "John", "Doe", "1234567890", "Address", "City", "12345", "License"), // Invalid postal code
                new BuyerDto("test@example.com", "John", "Doe", "1234567890", "Address", "City", "600001", null) // Null license number
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
