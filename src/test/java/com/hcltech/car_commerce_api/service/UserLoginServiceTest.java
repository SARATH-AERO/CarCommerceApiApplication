package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.AuthorityDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.dto.LoginDto;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MyUserDao myUserDao;

    @Mock
    private AuthorityDao authorityDao;

    @Mock
    private BuyerService buyerService;

    @InjectMocks
    private UserLoginService userLoginService;



    private BuyerDto buyerDto;
    private Authority authority;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyerDto = BuyerDto.builder()
                .password("password123")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        authority = new Authority();
        authority.setAuthorityRole("ROLE_BUYER");
    }

    @Test
    void testFindUserEmail_UserExists() {
      
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.of(new MyUser()));
        assertThrows(AlreadyExistException.class, () -> userLoginService.findUserEmail("john.doe@example.com"));
        verify(myUserDao).findByUsername(buyerDto.getEmail());
    }

    @Test
    void testFindUserEmail_UserDoesNotExist() {
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.empty());
        userLoginService.findUserEmail(buyerDto.getEmail());
        verify(myUserDao).findByUsername(buyerDto.getEmail());
    }

    @Test
    void testSetAuthority_Success() {
        when(passwordEncoder.encode(buyerDto.getPassword())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("mockJwtToken");
        LoginDto login = userLoginService.setAuthority("ROLE_BUYER", buyerDto.getPassword(), buyerDto.getEmail());
        verify(authorityDao).saveAuthority(any(Authority.class));
        verify(myUserDao).saveUser(any(MyUser.class));
        assertEquals("john.doe@example.com added successfully", login.getMessage());
        assertEquals("mockJwtToken", login.getJwtToken());
    }

    @Test
    void testGenerateJwt() {
        UserDetails userDetails = mock(UserDetails.class);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        String jwtToken = userLoginService.generateJwt(userDetails);
        assertEquals("mockJwtToken", jwtToken);
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void testSetUserDetail() {
        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(new SimpleGrantedAuthority("ROLE_BUYER"));
        UserDetails userDetails = userLoginService.setUserDetail("encodedPassword", buyerDto.getEmail(), authoritiesSet);
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
        
        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(authority);
        MyUser myUser = userLoginService.setUser("encodedPassword", buyerDto.getEmail(), authoritySet);
        assertEquals(buyerDto.getEmail(), myUser.getUsername());
        assertEquals("encodedPassword", myUser.getPassword());
        assertTrue(myUser.isEnabled());
        assertEquals(authoritySet, myUser.getAuthorities());
    }
    @Test
    void testCreateBuyer_AlreadyExists() {
        when(myUserDao.findByUsername(buyerDto.getEmail())).thenReturn(Optional.of(new MyUser()));

        Exception exception = assertThrows(AlreadyExistException.class, () -> {
            userLoginService.createBuyer(buyerDto, "ROLE_BUYER");
        });

        assertEquals("john.doe@example.com user email address already Exist.", exception.getMessage());
        verify(buyerService, never()).createBuyer(any());
    }
}
