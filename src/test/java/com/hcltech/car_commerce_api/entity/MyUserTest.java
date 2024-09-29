package com.hcltech.car_commerce_api.entity;

import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.repository.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class MyUserTest {


    @InjectMocks
    private MyUserDao myUserDao;
    

    @Mock
    private MyUserRepository myUserRepository;

    private MyUser myUser;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        myUser = new MyUser();
        myUser.setUsername("test@example.com");
    }


    @Test
    void testOnCreate() {
        myUser.onCreate();
        assertNotNull(myUser.getCreatedAt(), "createdAt should not be null");
        assertNotNull(myUser.getUpdatedAt(), "updatedAt should not be null");
        assertTrue(myUser.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(myUser.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        when(myUserRepository.save(myUser)).thenReturn(myUser);

        myUserDao.saveUser(myUser);

        verify(myUserRepository, times(1)).save(myUser);
    }

    @Test
    void testOnUpdate() throws InterruptedException {
        myUser.onCreate();
        when(myUserRepository.save(myUser)).thenReturn(myUser);
        myUserDao.saveUser(myUser);
        LocalDateTime initialUpdatedAt = myUser.getUpdatedAt();
        Thread.sleep(1000);
        myUser.onUpdate();
        assertTrue(myUser.getUpdatedAt().isAfter(initialUpdatedAt), "updatedAt should be updated");
        verify(myUserRepository, times(1)).save(myUser);

    }



}
