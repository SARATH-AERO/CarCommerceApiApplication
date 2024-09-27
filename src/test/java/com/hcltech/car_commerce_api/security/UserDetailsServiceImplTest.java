package com.hcltech.car_commerce_api.security;

import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.entity.MyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    @Mock
    private MyUserDao myUserDao;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {

        String username = "user@example.com";
        String password = "password123";
        MyUser myUser = MyUser.builder()
                .username(username)
                .password(password)
                .enabled(true)
                .authorities(new HashSet<>()) // Assuming you add authorities here
                .build();

        when(myUserDao.findByUsername(username)).thenReturn(Optional.of(myUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistent@example.com";
        when(myUserDao.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals(username + " user not found", exception.getMessage());
    }
}