package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dao.AuthorityDao;
import com.hcltech.car_commerce_api.dao.SellerDao;
import com.hcltech.car_commerce_api.dao.MyUserDao;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.*;
import com.hcltech.car_commerce_api.exception.BuyerEmailAlreadyExistsException;
import com.hcltech.car_commerce_api.exception.SellerNotFoundException;
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
public class SellerService {

    private final SellerDao sellerDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MyUserDao myUserDAO;
    private final AuthorityDao authorityDAO;


    @Autowired
    public SellerService(ModelMapper modelMapper, SellerDao sellerDao,
                         PasswordEncoder passwordEncoder, JwtUtil jwtUtil, MyUserDao myUserDAO, AuthorityDao authorityDAO){
        this.modelMapper = modelMapper;
        this.sellerDao = sellerDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.myUserDAO = myUserDAO;
        this.authorityDAO = authorityDAO;
    }

    public Map<String, String>  createUser(SellerDto sellerDto){

        Optional<Seller> seller =  sellerDao.getSellerByEmail(sellerDto.getEmail());
        if(seller.isPresent())
            throw new BuyerEmailAlreadyExistsException(sellerDto.getEmail() + " email address is already exists. Please choose a different email.");

        Authority buyerAuthority = new Authority();
        buyerAuthority.setAuthority("ROLE_SELLER");
        Set<Authority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(buyerAuthority);

        // Encode the password
        String encodedPassword = passwordEncoder.encode(sellerDto.getPassword());

        MyUser myUser = MyUser.builder()
                .username(sellerDto.getEmail())
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
        myUserDAO.saveUser(myUser);
        sellerDao.createSeller(toSellerEntity(sellerDto));

        final String jwtToken =  jwtUtil.generateToken(userDetails);
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("message", sellerDto.getFirstName()+" buyer added successfully");
        responseJson.put("token", jwtToken);
        return responseJson;
    }

    public Seller getSellerByEmail(String email){
        Optional<Seller> seller = sellerDao.getSellerByEmail(email);
        if(seller.isEmpty())
            throw new SellerNotFoundException("Seller not found with email: " + email);

        return seller.get();
    }

    public String udpateSeller(String email, SellerDto sellerDto) throws Exception {

        Optional<Seller> seller =  sellerDao.getSellerByEmail(sellerDto.getEmail());
        if(seller.isEmpty())
            throw new SellerNotFoundException("Seller not found with email: " + email);

        return sellerDao.updateSeller(email, sellerDto);
    }

    public String deleteSeller(String email) {
        int deletedCount =  sellerDao.deleteSeller(email);
        if(deletedCount == 0){
            throw new SellerNotFoundException("Seller not found with email: " + email);
        }
        return email+ " seller deleted successfully";
    }

    public SellerDto toSellerDto(Seller seller){
        return modelMapper.map(seller, SellerDto.class);
    }

    public Seller toSellerEntity(SellerDto sellerDto){
        return modelMapper.map(sellerDto, Seller.class);
    }


    public List<Cars> getAllCars() {
        List<Cars> cars = sellerDao.getAllCars();
        return cars;
    }

}
