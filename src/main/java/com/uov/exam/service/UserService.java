package com.uov.exam.service;

import com.uov.exam.Response.AuthResponse;
import com.uov.exam.dto.LoginRequest;
import com.uov.exam.dto.UserRegistrationRequest;
import com.uov.exam.model.User;
import org.springframework.security.core.Authentication;


public interface UserService {

    AuthResponse registerUser(UserRegistrationRequest userRegistrationRequest);

    AuthResponse loginUser(LoginRequest loginRequest);




}
