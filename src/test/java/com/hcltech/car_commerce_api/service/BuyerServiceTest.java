package com.hcltech.car_commerce_api.service;

package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.*;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.MessageDto;
import com.hcltech.car_commerce_api.dto.ResponseBuyerDto;
import com.hcltech.car_commerce_api.dto.UpdateBuyerDto;
import com.hcltech.car_commerce_api.entity.Authority;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.exception.BuyerEmailAlreadyExistsException;
import com.hcltech.car_commerce_api.exception.BuyerNotFoundException;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuyerServiceTest {

    @InjectMocks
    private BuyerService buyerService;

    @Mock
    private BuyerDao buyerDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MyUserDao myUserDAO;

    @Mock
    private AuthorityDao authorityDAO;

    @Mock
    private PurchasedCarDao purchasedCarDao;

    @Mock
    private CarDao carDao;

    @Mock
    private ModelMapper modelMapper;

    // Other required mocks and setup code

    @Test
    void testCreateBuyer_AlreadyExists() {
        BuyerDto buyerDto = new BuyerDto();
        buyerDto.setEmail("existing@example.com");

        // Given
        when(buyerDao.getBuyerByEmail(buyerDto.getEmail())).thenReturn(Optional.of(new Buyer()));

        // When & Then
        assertThrows(BuyerEmailAlreadyExistsException.class, () -> {
            buyerService.createBuyer(buyerDto);
        });

        verify(buyerDao, times(1)).getBuyerByEmail(buyerDto.getEmail());
    }

    @Test
    void testCreateBuyer_Success() {
        BuyerDto buyerDto = new BuyerDto();
        buyerDto.setEmail("new@example.com");
        buyerDto.setPassword("password");

        MyUser myUser = new MyUser();
        myUser.setUsername(buyerDto.getEmail());

        Authority buyerAuthority = new Authority();
        buyerAuthority.setAuthority("ROLE_BUYER");

        when(buyerDao.getBuyerByEmail(buyerDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(buyerDto.getPassword())).thenReturn("encodedPassword");
        when(modelMapper.map(buyerDto, Buyer.class)).thenReturn(new Buyer());
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwtToken");

        // When
        Map<String, String> response = buyerService.createBuyer(buyerDto);

        // Then
        assertEquals("success", response.get("status"));
        assertEquals("jwtToken", response.get("token"));
        verify(buyerDao, times(1)).createBuyer(any(Buyer.class));
        verify(authorityDAO, times(1)).saveAuthority(any(Authority.class));
        verify(myUserDAO, times(1)).saveUser(any(MyUser.class));
    }

    @Test
    void testGetBuyerByEmail_BuyerNotFound() {
        String email = "nonexistent@example.com";

        when(buyerDao.getBuyerByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BuyerNotFoundException.class, () -> {
            buyerService.getBuyerByEmail(email);
        });

        verify(buyerDao, times(1)).getBuyerByEmail(email);
    }

    @Test
    void testGetBuyerByEmail_Success() {
        String email = "buyer@example.com";
        Buyer buyer = new Buyer();
        buyer.setEmail(email);

        ResponseBuyerDto responseBuyerDto = new ResponseBuyerDto();
        responseBuyerDto.setEmail(email);

        when(buyerDao.getBuyerByEmail(email)).thenReturn(Optional.of(buyer));
        when(modelMapper.map(buyer, ResponseBuyerDto.class)).thenReturn(responseBuyerDto);

        ResponseBuyerDto result = buyerService.getBuyerByEmail(email);

        assertEquals(email, result.getEmail());
        verify(buyerDao, times(1)).getBuyerByEmail(email);
        verify(modelMapper, times(1)).map(buyer, ResponseBuyerDto.class);
    }

    @Test
    @DisplayName("Should update buyer when valid data is provided")
    void updateBuyer_shouldUpdateBuyer_WhenValidDataIsProvided() {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "123456", "123456789012345");
        Buyer existingBuyer = new Buyer(); // setup with initial data
        Buyer updatedBuyer = new Buyer(); // setup with updated data

        when(buyerDao.getBuyerByEmail(email)).thenReturn(Optional.of(existingBuyer));

        // Act
        MessageDto result = buyerService.updateBuyer(email, updateBuyerDto);

        // Assert
        verify(buyerDao).createBuyer(updatedBuyer); // Make sure the updated buyer was saved
        assertEquals(email + " buyer details updated successfully", result.getMessage());
    }

    @Test
    @DisplayName("Should throw NotFoundException when buyer does not exist")
    void updateBuyer_shouldThrowNotFoundException_WhenBuyerDoesNotExist() {
        // Arrange
        String email = "unknown@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "Doe", "1234567890", "Some Address", "123456", "123456789012345");

        when(buyerDao.getBuyerByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> buyerService.updateBuyer(email, updateBuyerDto));
    }

    @Test
    @DisplayName("Should not overwrite fields with null or empty values")
    void updateBuyer_shouldNotOverwriteFieldsWithNullOrEmptyValues() {
        // Arrange
        String email = "test@example.com";
        UpdateBuyerDto updateBuyerDto = new UpdateBuyerDto("John", "", null, "New Address", "", null);
//        Buyer existingBuyer = new Buyer("test@example.com", "sarath","sekar", "9876543210", "Old Address", "654321", "987654321098765");

        Buyer existingBuyer = Buyer.builder()
                .email("test@example.com")
                .firstName("sarath")
                .lastName("sekar")
                .phoneNumber("9876543210")
                .address("Old Address")
                .licenseNumber("987654321098765")
                .build();

        when(buyerDao.getBuyerByEmail(email)).thenReturn(Optional.of(existingBuyer));

        // Act
        buyerService.updateBuyer(email, updateBuyerDto);

        // Assert
        assertEquals("John", existingBuyer.getFirstName()); // Updated
        assertEquals("Buyer", existingBuyer.getLastName()); // Not updated
        assertEquals("9876543210", existingBuyer.getPhoneNumber()); // Not updated
        assertEquals("New Address", existingBuyer.getAddress()); // Updated
        assertEquals("654321", existingBuyer.getPostalCode()); // Not updated
        assertEquals("987654321098765", existingBuyer.getLicenseNumber()); // Not updated
    }

    @Test
    void testDeleteBuyer_BuyerNotFound() {
        String email = "nonexistent@example.com";

        when(buyerDao.deleteBuyer(email)).thenReturn(0);

        assertThrows(NotFoundException.class, () -> {
            buyerService.deleteBuyer(email);
        });

        verify(buyerDao, times(1)).deleteBuyer(email);
    }

    @Test
    void testDeleteBuyer_Success() {
        String email = "buyer@example.com";

        when(buyerDao.deleteBuyer(email)).thenReturn(1);

        Map<String, String> response = buyerService.deleteBuyer(email);

        assertEquals("success", response.get("status"));
        verify(buyerDao, times(1)).deleteBuyer(email);
    }




}