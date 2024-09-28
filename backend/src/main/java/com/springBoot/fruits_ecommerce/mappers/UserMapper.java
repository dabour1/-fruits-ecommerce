package com.springBoot.fruits_ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.models.RegistrationRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "email", source = "registrationRequest.email")
    @Mapping(target = "password", source = "registrationRequest.password")
    @Mapping(target = "username", source = "registrationRequest.username")
    User toEntity(RegistrationRequest registrationRequest);
}
