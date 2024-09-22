package com.hcltech.car_commerce_api.security;

import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {
        // Validate inputs
        if (request == null || response == null || filterChain == null) {
            throw new ServletException("Request, Response, and FilterChain cannot be null");
        }

        // Retrieve the Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        // Validate the presence and format of the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // Extract the token and username
            String jwtToken = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            // Authenticate the user if not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(jwtToken, username)) {
                    // Extract roles and create authorities
                    Collection<? extends GrantedAuthority> authorities = jwtUtil.extractRolesFromToken(jwtToken).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, authorities));
                }
            }
        }

        // Proceed to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
