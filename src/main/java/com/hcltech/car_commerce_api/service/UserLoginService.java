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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserLoginService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MyUserDao myUserDao;
    private final AuthorityDao authorityDao;
    private final SellerService sellerService;
    private final BuyerService buyerService;

    public UserLoginService(PasswordEncoder passwordEncoder,
                            JwtUtil jwtUtil, MyUserDao myUserDao,
                            AuthorityDao authorityDao,
                            SellerService sellerService,
                            BuyerService buyerService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.myUserDao = myUserDao;
        this.authorityDao = authorityDao;
        this.sellerService = sellerService;
        this.buyerService = buyerService;
    }


    public LoginDto createBuyer(BuyerDto buyerDto,String role){
        findUserEmail(buyerDto.getEmail());
        buyerService.findBuyerByEmail(buyerDto.getEmail());
        buyerService .createBuyer(buyerDto);
        return setAuthority (role,buyerDto.getPassword(), buyerDto.getEmail());
    }

    private void findUserEmail(String email){
        Optional<MyUser> byUsername = myUserDao.findByUsername(email);
        if (byUsername.isPresent())
            throw new AlreadyExistException(email + " user email address");
    }

    public LoginDto createSeller(SellerDto sellerDto,String role){
        findUserEmail(sellerDto.getEmail());
        sellerService.findSellerByEmail(sellerDto.getEmail());
        sellerService .createSeller(sellerDto);
        return setAuthority (role,sellerDto.getPassword(), sellerDto.getEmail());
    }
    private LoginDto setAuthority(String role,String password ,String emailId){
        Authority authority = new Authority();
        authority.setAuthorityRole(role);
        Set<Authority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(authority);

        String encodedPassword = passwordEncoder.encode(password);
        MyUser myUser = setUser(encodedPassword,emailId,authoritiesSet);
        authorityDao.saveAuthority(authority);
        myUserDao.saveUser(myUser);

        return LoginDto.builder().message(emailId +" added successfully").jwtToken(
                        generateJwt(setUserDetail(encodedPassword,emailId, setGrantedAuthority(myUser))))
                       .build();
    }

    public String generateJwt(UserDetails userDetails){
        return  jwtUtil.generateToken(userDetails);
    }

    private UserDetails setUserDetail(String password,
                                      String emailId,
                                      Set<SimpleGrantedAuthority> authoritySet){
        return new User(emailId,
                password,
                true,
                true,
                true,
                true,
                authoritySet
        );

    }

    private Set<SimpleGrantedAuthority> setGrantedAuthority(MyUser user){
        return user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityRole()))
                .collect(Collectors.toSet());
    }

    private MyUser setUser(String password ,String emailId,Set<Authority> authoritySet) {
        return   MyUser.builder()
                .username(emailId)
                .password(password)
                .enabled(true)
                .authorities(authoritySet).
                build();
    }


}
