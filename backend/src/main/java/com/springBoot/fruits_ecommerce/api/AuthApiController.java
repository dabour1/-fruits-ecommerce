package com.springBoot.fruits_ecommerce.api;

import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.RegistrationRequest;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.mappers.MapRegistrationRequest;
import com.springBoot.fruits_ecommerce.services.AuthenticationService;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;

import javax.validation.Valid;

import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-09-28T07:25:00.809527300+03:00[Asia/Riyadh]", comments = "Generator version: 7.8.0")
@Controller
@RequestMapping("${openapi.authentication.base-path:/api}")
public class AuthApiController implements AuthApi {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiController.class);
    private final NativeWebRequest request;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private MapRegistrationRequest mapRegistrationRequest;

    @Autowired
    public AuthApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticat(@Valid @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticat(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegistrationRequest registrationRequest) {

        // User userEntity = mapRegistrationRequest.toEntity(registrationRequest);

        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

}
