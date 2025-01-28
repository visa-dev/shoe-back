package com.uov.exam.controller;

import com.uov.exam.Response.AuthResponse;
import com.uov.exam.Response.CommonResponse;
import com.uov.exam.config.JwtUtils;
import com.uov.exam.dto.LoginRequest;
import com.uov.exam.dto.UserRegistrationRequest;
import com.uov.exam.model.User;
import com.uov.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {

        AuthResponse authResponse= userService.registerUser(userRegistrationRequest);  // Save the user in the database

        if (authResponse!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error login user");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse= userService.loginUser(loginRequest);  // Save the user in the database

        if (authResponse!=null){
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error login user");
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String jwt) {
        User user = jwtUtils.getCurrentUserByJwtToken(jwt.substring(7));
        if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null , HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        boolean isValid = jwtUtils.validateToken(jwt);

        if (isValid) {
            return new ResponseEntity<>("Success: Token Valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Token Invalid", HttpStatus.UNAUTHORIZED);
        }
    }

}
