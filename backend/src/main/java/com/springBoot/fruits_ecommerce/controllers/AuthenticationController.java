package com.springBoot.fruits_ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.services.AuthenticationService;

import jakarta.validation.Valid;
 

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register( @Valid @RequestBody User user) throws Exception {
        return ResponseEntity.ok(authenticationService.register(user)) ;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody  AuthenticationRequest request) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticat(  request)) ;
    }
}
