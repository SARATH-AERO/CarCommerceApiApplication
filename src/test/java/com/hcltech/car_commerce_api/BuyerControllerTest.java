//package com.hcltech.car_commerce_api;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hcltech.car_commerce_api.dto.BuyerDTO;
//import com.hcltech.car_commerce_api.entity.Buyer;
//import com.hcltech.car_commerce_api.service.BuyerService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class BuyerControllerTest {
//
//    private MockMvc mockMvc;
//
//    private BuyerService buyerService;
//
//    public BuyerControllerTest(MockMvc mockMvc, BuyerService buyerService) {
//        this.mockMvc = mockMvc;
//        this.buyerService = buyerService;
//    }
//
//    @Test
//    void createUser_shouldReturnOkStatus() throws Exception {
//        // Arrange
//        BuyerDTO buyerDTO = new BuyerDTO(/* initialize fields */);
//        String expectedResponse = "Buyer created successfully";
//
//        when(buyerService.createUser(any(BuyerDTO.class))).thenReturn(expectedResponse);
//
//        // Act and Assert
//        mockMvc.perform(post("/buyer")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(buyerDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(expectedResponse));
//    }
//
//    @Test
//    void getBuyerByEmail_shouldReturnBuyer() throws Exception {
//        // Arrange
//        String email = "test@example.com";
//        Buyer buyer = new Buyer(/* initialize fields */);
//
//        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
//
//        // Act and Assert
//        mockMvc.perform(get("/buyer").param("email", email))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value(email)); // Verify returned JSON data
//    }
//
//    @Test
//    void updateUser_shouldReturnOkStatus() throws Exception {
//        // Arrange
//        String email = "test@example.com";
//        BuyerDTO buyerDTO = new BuyerDTO(/* initialize fields */);
//        String expectedResponse = "Buyer updated successfully";
//
//        when(buyerService.udpateBuyer(eq(email), any(BuyerDTO.class))).thenReturn(expectedResponse);
//
//        // Act and Assert
//        mockMvc.perform(put("/buyer")
//                        .param("email", email)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(buyerDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(expectedResponse));
//    }
//
//    @Test
//    void deleteBuyer_shouldReturnOkStatus() throws Exception {
//        // Arrange
//        String email = "test@example.com";
//        String expectedResponse = "Buyer deleted successfully";
//
//        when(buyerService.deleteBuyer(email)).thenReturn(expectedResponse);
//
//        // Act and Assert
//        mockMvc.perform(delete("/buyer").param("email", email))
//                .andExpect(status().isOk())
//                .andExpect(content().string(expectedResponse));
//    }
//
//    @Test
//    void createUser_shouldReturnBadRequestForInvalidInput() throws Exception {
//        // Arrange
//        BuyerDTO invalidBuyerDTO = new BuyerDTO();  // Missing required fields
//
//        // Act and Assert
//        mockMvc.perform(post("/buyer")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(invalidBuyerDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void getBuyerByEmail_shouldReturnNotFoundWhenExceptionThrown() throws Exception {
//        // Arrange
//        String email = "unknown@example.com";
//
//        when(buyerService.getBuyerByEmail(email)).thenThrow(new EntityNotFoundException("Buyer not found"));
//
//        // Act and Assert
//        mockMvc.perform(get("/buyer").param("email", email))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Buyer not found"));
//    }
//
//
//
//}
