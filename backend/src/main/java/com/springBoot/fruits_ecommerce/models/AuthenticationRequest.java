package com.springBoot.fruits_ecommerce.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequest {
  @NotBlank(message = "email is required")
  @NotNull(message = "email cannot be null")
  @Email(message = "Email should be valid")
  private String email;
  @NotBlank(message = "password is required")
  @NotNull(message = "password cannot be null")
  String password;
}
