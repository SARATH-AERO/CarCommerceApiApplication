package com.hcltech.car_commerce_api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.*;

import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SellerControllerTest {

    @Mock
    private SellerService sellerService;
    @InjectMocks
    private SellerController sellerController;
    private MockMvc mockMvc;
    private ResponseSellerDto responseSellerDto;
    private MessageDto messageDto;
    private String email;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sellerController).build();

        email = "jane.doe@example.com";

        responseSellerDto = new ResponseSellerDto();
        responseSellerDto.setFirstName("Jane");
        responseSellerDto.setLastName("Doe");
        responseSellerDto.setEmail(email);
        responseSellerDto.setAddress("Chennai");
        responseSellerDto.setPhoneNumber("1234567890");
        responseSellerDto.setPostalCode("600094");

        messageDto = MessageDto.builder().message("Success").build();
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void testGetSellerByEmail_Success() throws Exception {
        when(sellerService.getSellerByEmail(email)).thenReturn(responseSellerDto);

        mockMvc.perform(get("/api/carCommerceApi/v1/seller/email")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(sellerService, times(1)).getSellerByEmail(email);
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void testGetSellerByEmail_NotFound() throws Exception {
        when(sellerService.getSellerByEmail("check@gmail.com")).thenThrow(new NotFoundException("Seller email: " + email));

        mockMvc.perform(get("/api/carCommerceApi/v1/seller/email")
                        .param("email", email))
                .andExpect(status().isOk());
        verify(sellerService, times(1)).getSellerByEmail(email);
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void testDeleteSeller_Success() throws Exception {
        when(sellerService.deleteSeller(email)).thenReturn(messageDto);

        mockMvc.perform(delete("/api/carCommerceApi/v1/seller")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Success"));

        verify(sellerService, times(1)).deleteSeller(email);
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void testDeleteSeller_NotFound() throws Exception {
        when(sellerService.deleteSeller("check@gmail.com")).thenThrow(new NotFoundException("Seller email: " + email));

        mockMvc.perform(delete("/api/carCommerceApi/v1/seller")
                        .param("email", email))
                .andExpect(status().isOk());

        verify(sellerService, times(1)).deleteSeller(email);
    }
}

