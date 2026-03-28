package com.backend.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {

    @NotEmpty(message = "First Name should not Empty")
    private String firstName;

    @NotEmpty(message = "Last Name should not Empty")
    private String lastName;

    @NotEmpty(message = "Email should not Empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not Empty")
    private String password;
}
