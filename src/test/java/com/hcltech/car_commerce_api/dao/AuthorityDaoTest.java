package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.entity.Authority;
import com.hcltech.car_commerce_api.repository.AuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorityDaoTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityDao authorityDao;

    private Authority authority;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authority = new Authority(); // Initialize Authority object with fields if needed
    }

    @Test
    void testSaveAuthority() {
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);
        authorityDao.saveAuthority(authority);
        verify(authorityRepository, times(1)).save(authority);
    }
}
