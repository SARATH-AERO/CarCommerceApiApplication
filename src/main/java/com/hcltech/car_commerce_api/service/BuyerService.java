package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.AuthorityDao;
import com.hcltech.car_commerce_api.dao.BuyerDao;
import com.hcltech.car_commerce_api.dao.UserDAO;
import com.hcltech.car_commerce_api.dto.BuyerDto;
import com.hcltech.car_commerce_api.entity.Authority;
import com.hcltech.car_commerce_api.entity.Buyer;
import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.exception.BuyerEmailAlreadyExistsException;
import com.hcltech.car_commerce_api.exception.BuyerNotFoundException;
import com.hcltech.car_commerce_api.security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BuyerService {

    private final ModelMapper modelMapper;
    private final BuyerDao buyerDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDAO userDAO;
    private final AuthorityDao authorityDAO;


    @Autowired
    public BuyerService(ModelMapper modelMapper, BuyerDao buyerDAO, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserDAO userDAO, AuthorityDao authorityDAO){
        this.modelMapper = modelMapper;
        this.buyerDAO = buyerDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDAO = userDAO;
        this.authorityDAO = authorityDAO;
    }

    public Map<String, String> createBuyer(BuyerDto buyerDto){

        Optional<Buyer> buyer =  buyerDAO.getBuyerByEmail(buyerDto.getEmail());
        if(buyer.isPresent())
            throw new BuyerEmailAlreadyExistsException(buyerDto.getEmail() + " email address is already exists. Please choose a different email.");

        Authority buyerAuthority = new Authority();
        buyerAuthority.setAuthority("ROLE_BUYER");
        Set<Authority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(buyerAuthority);

        // Encode the password
        String encodedPassword = passwordEncoder.encode(buyerDto.getPassword());

        MyUser myUser = MyUser.builder()
                        .username(buyerDto.getEmail())
                        .password(encodedPassword)
                        .enabled(true)
                        .authorities(authoritiesSet).
                        build();

        Set<SimpleGrantedAuthority> grantedAuthoritySet = myUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());

        UserDetails userDetails = new User(myUser.getUsername(),
                encodedPassword,
                myUser.isEnabled(),
                true,
                true,
                true,
                grantedAuthoritySet
                );

        authorityDAO.saveAuthority(buyerAuthority);
        userDAO.saveUser(myUser);
        buyerDAO.createBuyer(toBuyerEntity(buyerDto));

         final String jwtToken =  jwtUtil.generateToken(userDetails);
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("message", buyerDto.getFirstName()+" buyer added successfully");
        responseJson.put("token", jwtToken);
         return responseJson;

    }

    public Buyer getBuyerByEmail(String email){
        Optional<Buyer> buyer = buyerDAO.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new BuyerNotFoundException("Buyer not found with email: " + email);

        return buyer.get();
    }

    public Map<String,String> updateBuyer(String email, BuyerDto buyerDTO) throws Exception {

        Optional<Buyer> buyer =  buyerDAO.getBuyerByEmail(email);
        if(buyer.isEmpty())
            throw new BuyerNotFoundException("Buyer not found with email: " + email);

        buyerDAO.updateBuyer(email, buyerDTO);
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("message", email+" buyer details updated successfully");
        return responseJson;
    }

    public Map<String, String> deleteBuyer(String email) {
       int deletedCount =  buyerDAO.deleteBuyer(email);
       if(deletedCount == 0){
           throw new BuyerNotFoundException("Buyer not found with email: " + email);
       }

        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("message", email+ " buyer deleted successfully");
        return responseJson;
    }

    public Buyer toBuyerEntity(BuyerDto buyerDTO){
        return modelMapper.map(buyerDTO, Buyer.class);
    }

}
