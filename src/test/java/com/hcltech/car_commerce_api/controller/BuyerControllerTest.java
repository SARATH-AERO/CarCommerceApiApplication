package com.hcltech.car_commerce_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.configuration.TestSecurityConfig;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.service.BuyerService;
import com.hcltech.car_commerce_api.service.UserLoginService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(BuyerController.class)
class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuyerService buyerService;

    @MockBean
    private UserLoginService userLoginService;


    @Test
    @DisplayName("POST /api/authentication/buyer - Should create buyer and return Created status")
    void createBuyer_shouldReturnCreatedStatus() throws Exception {
        // Arrange
        BuyerDto buyerDto = new BuyerDto(
                "test@example.com",
                "password",
                "John",
                "Doe",
                "1234567890",
                "Address",
                "600001",
                "1234567890123456"
        );

        LoginDto loginDto = LoginDto.builder()
                .message(" added successfully")
                .build();

        when(userLoginService.createBuyer(buyerDto, "ROLE_BUYER")).thenReturn(loginDto);

        // Act and Assert
        mockMvc.perform(post("/api/authentication/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(buyerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Buyer created successfully"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("GET /api/buyer - Should return buyer details along with purchased car details for a valid email")
    void getBuyerByEmail_shouldReturnBuyerDetailsAndPurchasedCar() throws Exception {
        // Arrange
        String email = "test@example.com";
        ResponseBuyerDto buyer = ResponseBuyerDto.builder()
                .email(email)
                .firstName("Sarath")
                .lastName("Sekar")
                .address("Village Road, Sholinganallur")
                .phoneNumber("8985623412")
                .postalCode("600119")
                .licenseNumber("1234567890123456")
                .build();

        PurchasedCarDto purchasedCarDto = PurchasedCarDto.builder()
                .brand("BMW")
                .carName("Grand Turismo")
                .colour("yellow")
                .engineNumber("12345678901234567")
                .price(20000000)
                .model("GT")
                .purchasedDate(LocalDateTime.now())
                .build();

        // Add purchased car to buyer's list
        buyer.getPurchasedCarsList().add(purchasedCarDto);

        // Mocking the service response
        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);

        // Act and Assert
        mockMvc.perform(get("/api/buyer").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("Sarath"))
                .andExpect(jsonPath("$.lastName").value("Sekar"))
                .andExpect(jsonPath("$.address").value("Village Road, Sholinganallur"))
                .andExpect(jsonPath("$.phoneNumber").value("8985623412"))
                .andExpect(jsonPath("$.postalCode").value("600119"))
                .andExpect(jsonPath("$.licenseNumber").value("1234567890123456"))

                // Check purchased car details
                .andExpect(jsonPath("$.purchasedCarsList[0].brand").value("BMW"))
                .andExpect(jsonPath("$.purchasedCarsList[0].carName").value("Grand Turismo"))
                .andExpect(jsonPath("$.purchasedCarsList[0].colour").value("yellow"))
                .andExpect(jsonPath("$.purchasedCarsList[0].engineNumber").value("12345678901234567"))
                .andExpect(jsonPath("$.purchasedCarsList[0].price").value(20000000))
                .andExpect(jsonPath("$.purchasedCarsList[0].model").value("GT"))
                .andExpect(jsonPath("$.purchasedCarsList[0].purchasedDate").exists());
    }

    @Test
    @DisplayName("PUT /api/buyer - Should update buyer when valid input is provided")
    void updateBuyer_shouldUpdateBuyer_WhenValidInputIsProvided() throws Exception {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "123456", "123456789012345");

        // Mock service response
        when(buyerService.updateBuyer(eq(email), any(UpdateBuyerDto.class)))
                .thenReturn(new MessageDto(email + " buyer details updated successfully"));

        // Act & Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBuyerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(email + " buyer details updated successfully"));
    }

    @Test
    @DisplayName("PUT /api/buyer - Should return 400 Bad Request when invalid phone number is provided")
    void updateBuyer_shouldReturnBadRequest_WhenInvalidPhoneNumberIsProvided() throws Exception {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "12345", "Some Address", "123456", "123456789012345");

        // Act & Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBuyerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Phone number should be 10 digits and contain only numbers"));
    }

    @Test
    @DisplayName("PUT /api/buyer - Should return 404 Not Found when buyer does not exist")
    void updateBuyer_shouldReturnNotFound_WhenBuyerDoesNotExist() throws Exception {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "123456", "123456789012345");

        // Mock service throwing NotFoundException
        when(buyerService.updateBuyer(eq(email), any(UpdateBuyerDto.class)))
                .thenThrow(new NotFoundException(email + " buyer not present"));

        // Act & Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBuyerDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(email + " buyer not present"));
    }

    @Test
    @DisplayName("PUT /api/buyer - Should return 400 Bad Request when invalid postal code is provided")
    void updateBuyer_shouldReturnBadRequest_WhenInvalidPostalCodeIsProvided() throws Exception {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "12345", "123456789012345");

        // Act & Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBuyerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Postal Code should be 6 digits and contain only numbers"));
    }

    @Test
    @DisplayName("PUT /api/buyer - Should return 400 Bad Request when invalid license number is provided")
    void updateBuyer_shouldReturnBadRequest_WhenInvalidLicenseNumberIsProvided() throws Exception {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "123456", "123456789");

        // Act & Assert
        mockMvc.perform(put("/api/buyer")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBuyerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("License number must be exactly 15 characters long"));
    }



    @Test
    @DisplayName("DELETE /api/buyer - Should delete buyer and return success status")
    void deleteBuyer_shouldReturnSuccessStatus() throws Exception {
        // Arrange
        String email = "test@example.com";
        LoginDto loginDto = LoginDto.builder()
                .message(email + " buyer deleted successfully")
                .build();

//        when(buyerService.deleteBuyer(email)).thenReturn(loginDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/buyer")
                        .param("email", email))
                .andExpect(status().isOk()) // Verify that the status is 200 OK
                .andExpect(jsonPath("$.status").value("success")) // Verify status field
                .andExpect(jsonPath("$.message").value(email + " buyer deleted successfully")); // Verify message field
    }

    @Test
    @DisplayName("POST /api/authentication/buyer - Should return 400 Bad Request for invalid BuyerDto inputs")
    void createBuyer_shouldReturnBadRequestForInvalidInput() throws Exception {
        // Arrange: List of invalid BuyerDto objects for various invalid inputs
        List<BuyerDto> invalidBuyerDtos = Arrays.asList(
                // Invalid email cases
                new BuyerDto(null, "password", "John", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Null email
                new BuyerDto("invalid-email", "password", "John", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Invalid email format

                // Invalid first name
                new BuyerDto("test@example.com", "password", null, "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Null first name
                new BuyerDto("test@example.com", "password", "", "Doe", "1234567890", "Address", "600001", "1234567890123456"), // Empty first name

                // Invalid last name
                new BuyerDto("test@example.com", "password", "John", null, "1234567890", "Address", "600001", "1234567890123456"), // Null last name
                new BuyerDto("test@example.com", "password", "John", "", "1234567890", "Address", "600001", "1234567890123456"), // Empty last name

                // Invalid phone number
                new BuyerDto("test@example.com", "password", "John", "Doe", null, "Address", "600001", "1234567890123456"), // Null phone number
                new BuyerDto("test@example.com", "password", "John", "Doe", "12345", "Address", "600001", "1234567890123456"), // Invalid phone number format
                new BuyerDto("test@example.com", "password", "John", "Doe", "12345678901", "Address", "600001", "1234567890123456"), // Too long phone number

                // Invalid address
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", null, "600001", "1234567890123456"), // Null address
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "", "600001", "1234567890123456"), // Empty address

                // Invalid postal code
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", null, "1234567890123456"), // Null postal code
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "12345", "1234567890123456"), // Invalid postal code format
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "1234567", "1234567890123456"), // Too long postal code

                // Invalid license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", null), // Null license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", ""), // Empty license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", "12345678901234567"), // Too long license number
                new BuyerDto("test@example.com", "password", "John", "Doe", "1234567890", "Address", "600001", "12345678") // Too short license number
        );

        // Act and Assert: For each invalid BuyerDto, ensure a 400 Bad Request is returned
        for (BuyerDto invalidBuyerDto : invalidBuyerDtos) {
            mockMvc.perform(post("/api/authentication/buyer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invalidBuyerDto)))
                    .andExpect(status().isBadRequest()) // Ensure status is 400 Bad Request
                    .andExpect(jsonPath("$.message").value("Bad Request"))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }



    @Test
    @DisplayName("GET /api/buyer - Should return 404 Not Found when buyer does not exist")
    void getBuyerByEmail_shouldReturnNotFoundWhenExceptionThrown() throws Exception {
        // Arrange
        String email = "unknown@example.com";
        String expectedResponse = "Buyer email: " + email+" not Found";

        // Simulate buyerService throwing NotFoundException
        when(buyerService.getBuyerByEmail(email)).thenThrow(new NotFoundException(expectedResponse));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/buyer")
                        .param("email", email))
                .andExpect(status().isNotFound()) // Check if status is 404
                .andExpect(jsonPath("$.detail").value(expectedResponse)) // Check if error message matches
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }
}
