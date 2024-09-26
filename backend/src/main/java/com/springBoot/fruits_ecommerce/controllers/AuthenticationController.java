package com.springBoot.fruits_ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.fruits_ecommerce.DTO.UserDTO;
import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.services.AuthenticationService;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserDTO userDTO) throws Exception {
        User userEntity = modelMapper.map(userDTO, User.class);
        return ResponseEntity.ok(authenticationService.register(userEntity));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request)
            throws Exception {
        return ResponseEntity.ok(authenticationService.authenticat(request));
    }
}
