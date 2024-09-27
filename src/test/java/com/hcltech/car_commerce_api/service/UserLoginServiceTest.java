package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.AuthorityDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Authority;
import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.exception.AlreadyExistException;
import com.hcltech.car_commerce_api.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserLoginServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MyUserDao myUserDao;

    @Mock
    private AuthorityDao authorityDao;

    @Mock
    private SellerService sellerService;

    @Mock
    private BuyerService buyerService;

    @InjectMocks
    private UserLoginService userLoginService;

    private SellerDto sellerDto;


    private BuyerDto buyerDto;
    private Authority authority;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the BuyerDto and Authority for testing
        buyerDto = BuyerDto.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123") // Assuming password is part of BuyerDto
                .build();

        sellerDto = new SellerDto("jane.doe@example.com", "Jane", "Doe", "0987654321", "pass", "456 Elm St", "628362");

        authority = new Authority();
        authority.setAuthorityRole("ROLE_BUYER");
    }

    @Test
    void testFindUserEmail_UserExists() {
        // Arrange
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.of(new MyUser()));

        // Act & Assert
        assertThrows(AlreadyExistException.class, () -> userLoginService.findUserEmail(buyerDto.getEmail()));
        verify(myUserDao).findByUsername(buyerDto.getEmail());
    }

    @Test
    void testFindUserEmail_UserDoesNotExist() {
        // Arrange
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.empty());

        // Act
        userLoginService.findUserEmail(buyerDto.getEmail());

        // Assert
        verify(myUserDao).findByUsername(buyerDto.getEmail());
    }

    @Test
    void testSetAuthority_Success() {
        // Arrange
        when(passwordEncoder.encode(buyerDto.getPassword())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("mockJwtToken");

        // Act
        LoginDto loginDto = userLoginService.setAuthority("ROLE_BUYER", buyerDto.getPassword(), buyerDto.getEmail());

        // Assert
        verify(authorityDao).saveAuthority(any(Authority.class)); // Verify authority is saved
        verify(myUserDao).saveUser(any(MyUser.class)); // Verify MyUser is saved
        assertEquals("john.doe@example.com added successfully", loginDto.getMessage());
        assertEquals("mockJwtToken", loginDto.getJwtToken());
    }

    @Test
    void testGenerateJwt() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");

        // Act
        String jwtToken = userLoginService.generateJwt(userDetails);

        // Assert
        assertEquals("mockJwtToken", jwtToken);
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void testSetUserDetail() {
        // Arrange
        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(new SimpleGrantedAuthority("ROLE_BUYER"));

        // Act
        UserDetails userDetails = userLoginService.setUserDetail("encodedPassword", buyerDto.getEmail(), authoritiesSet);

        // Assert
        assertEquals(buyerDto.getEmail(), userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertEquals(authoritiesSet, userDetails.getAuthorities());
    }



    @Test
    void testSetUser() {
        // Arrange
        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(authority);

        // Act
        MyUser myUser = userLoginService.setUser("encodedPassword", buyerDto.getEmail(), authoritySet);

        // Assert
        assertEquals(buyerDto.getEmail(), myUser.getUsername());
        assertEquals("encodedPassword", myUser.getPassword());
        assertTrue(myUser.isEnabled());
        assertEquals(authoritySet, myUser.getAuthorities());
    }
    @Test
    public void testCreateBuyer_AlreadyExists() {
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.of(new MyUser()));

        Exception exception = assertThrows(AlreadyExistException.class, () -> {
            userLoginService.createBuyer(buyerDto, "ROLE_BUYER");
        });

        assertEquals("john.doe@example.com user email address already Exist.", exception.getMessage());
        verify(buyerService, never()).createBuyer(any());
    }


    @Test
    public void testCreateSeller_AlreadyExists() {
        when(myUserDao.findByUsername(sellerDto.getEmail())).thenReturn(Optional.of(new MyUser()));

        Exception exception = assertThrows(AlreadyExistException.class, () -> {
            userLoginService.createSeller(sellerDto, "ROLE_SELLER");
        });

        assertEquals("jane.doe@example.com user email address already Exist.", exception.getMessage());
        verify(sellerService, never()).createSeller(any());
    }
}
