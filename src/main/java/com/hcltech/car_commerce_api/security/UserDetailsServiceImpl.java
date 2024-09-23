package com.hcltech.car_commerce_api.security;

import com.hcltech.car_commerce_api.entity.MyUser;
import com.hcltech.car_commerce_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        MyUser myUser = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username +" user not found"));

        Set<SimpleGrantedAuthority> authoritySet = myUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());

        return new User(
                myUser.getUsername(),
                myUser.getPassword(),
                myUser.isEnabled(),
                true,
                true,
                true,
                authoritySet
        ) {
        };

    }
}
