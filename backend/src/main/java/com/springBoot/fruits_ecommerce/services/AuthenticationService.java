package com.springBoot.fruits_ecommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 

import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.repositorys.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
     @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    
    public AuthenticationResponse register(User request  ){
        Optional<User>   userNameExisting = userRepository.findByUsername(request.getUsername());
        if (userNameExisting.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Optional<User>   emailExisting = userRepository.findByEmail(request.getEmail());
        if (emailExisting.isPresent()) {
            
            throw new IllegalArgumentException("Email already exists");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userRepository.save(request);
        final String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);

    }

    public AuthenticationResponse authenticat(User request) throws Exception  {
         
    }



}
