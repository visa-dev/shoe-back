package com.uov.exam.dto;

import com.uov.exam.model.USER_ROLE;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "User name is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private USER_ROLE role;

    private String name;
    private String address;
    private String email;
    private String mobile;


}
