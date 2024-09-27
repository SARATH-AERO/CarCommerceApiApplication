package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.repository.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserDaoTest {

    @Mock
    private MyUserRepository myUserRepository;

    @InjectMocks
    private MyUserDao myUserDao;

    private MyUser mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new MyUser();
        mockUser.setUsername("johndoe@example.com");
        mockUser.setPassword("password123");
    }

    @Test
    void testFindByUsername_Success() {
        when(myUserRepository.findByUsername("johndoe@example.com")).thenReturn(Optional.of(mockUser));

        Optional<MyUser> result = myUserDao.findByUsername("johndoe@example.com");

        assertTrue(result.isPresent());
        assertEquals("johndoe@example.com", result.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        when(myUserRepository.findByUsername("unknown@example.com")).thenReturn(Optional.empty());

        Optional<MyUser> result = myUserDao.findByUsername("unknown@example.com");

        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUser() {
        myUserDao.saveUser(mockUser);

        verify(myUserRepository, times(1)).save(mockUser);
    }

    @Test
    void testDeleteUser() {
        myUserDao.deleteUser("johndoe@example.com");

        verify(myUserRepository, times(1)).deleteByUsername("johndoe@example.com");
    }
}
