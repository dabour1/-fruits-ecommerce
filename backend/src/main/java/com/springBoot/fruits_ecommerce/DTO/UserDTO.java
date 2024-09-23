package com.springBoot.fruits_ecommerce.DTO;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class UserDTO {
    @NotBlank(message = "username is required")
    @NotNull(message = "username cannot be null")
    private String username;

    @NotBlank(message = "email is required")
    @NotNull(message = "email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "password is required")
    @NotNull(message = "password cannot be null")
    @Size(min = 8, max = 250, message = "password must be between 8 and 50 characters")
    private String password;

 
}

