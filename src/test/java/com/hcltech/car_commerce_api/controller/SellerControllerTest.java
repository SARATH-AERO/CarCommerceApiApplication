package com.hcltech.car_commerce_api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.car_commerce_api.dto.*;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

class SellerControllerTest {

    @Mock
    private SellerService sellerService;
    @InjectMocks
    private SellerController sellerController;
    private MockMvc mockMvc;
    private ResponseSellerDto responseSellerDto;
    private MessageDto messageDto;
    private String email;

    private  CarDto carDto;

    private UpdateSellerDto updateSellerDto;

    private static final ObjectMapper mapper = new ObjectMapper();


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

        updateSellerDto = UpdateSellerDto.builder()
                .firstName("sai").build();

        carDto = CarDto.builder().brand("AUDI").engineNumber("12345678901234567").build();
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
    @Test
    @WithMockUser(roles = "SELLER")
    void testUpdateUser()  throws Exception{

        when(sellerService.updateSeller(eq(email), any(UpdateSellerDto.class)))
                .thenReturn(messageDto);

        mockMvc.perform(put("/api/carCommerceApi/v1/seller/update")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateSellerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(sellerService, times(1)).updateSeller(eq(email),
                any(UpdateSellerDto.class));
    }


    @Test
    @WithMockUser(roles =  "SELLER")
    void testAddSellerCar()  throws Exception{

        when(sellerService.addSellerCar(eq(email), any(CarDto.class))).thenReturn(messageDto);

        mockMvc.perform(put("/api/carCommerceApi/v1/seller")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(sellerService, times(1)).addSellerCar(eq(email), any(CarDto.class));
    }


    @Test
    @WithMockUser(roles = "SELLER")
    void testUpdateCar() throws Exception {
        int carId = 1;
        when(sellerService.updateCar(eq(email), eq(carId), any(CarDto.class)))
                .thenReturn(messageDto);

        mockMvc.perform(put("/api/carCommerceApi/v1/seller/updateCar")
                        .param("email", email)
                        .param("id", String.valueOf(carId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(sellerService, times(1)).updateCar(eq(email), eq(carId), any(CarDto.class));
    }
}

