package com.uov.exam.service.implementation;

import com.uov.exam.Exception.UserAlreadyExistsException;
import com.uov.exam.Exception.UserRoleInvalidException;
import com.uov.exam.Response.AuthResponse;
import com.uov.exam.config.JwtUtils;
import com.uov.exam.dto.LoginRequest;
import com.uov.exam.dto.UserRegistrationRequest;
import com.uov.exam.model.USER_ROLE;
import com.uov.exam.model.User;
import com.uov.exam.repo.UserRepo;
import com.uov.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerUser(UserRegistrationRequest userRegistrationRequest) {


        if (userRepository.existsByUsername(userRegistrationRequest.getUsername())) {
            throw new UserAlreadyExistsException("User with given regNo already exists");
        }

            USER_ROLE role = userRegistrationRequest.getRole();
            User user = createUser(userRegistrationRequest, role);
            userRepository.save(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString())));
            SecurityContextHolder.getContext().setAuthentication(auth);


            String jwt = jwtUtils.generateJwtToken(auth);
            AuthResponse authResponse =new AuthResponse();
            authResponse.setJwt(jwt);
            authResponse.setRole(role);
            authResponse.setUsername(user.getUsername());
            authResponse.setMessage("Successfully registered user");

            return authResponse;

    }

    private User createUser(UserRegistrationRequest userRegistrationRequest, USER_ROLE role) {

        User user = new User();
        user.setUsername(userRegistrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setRole(role);
        return user;
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication auth =authenticateUser(username,password);

        Collection<? extends GrantedAuthority> authorities=auth.getAuthorities();
        String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtUtils.generateJwtToken(auth);
        AuthResponse authResponse =new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUsername(username);
        authResponse.setMessage("Successfully logged in");
        assert role != null;
        authResponse.setRole(USER_ROLE.valueOf(role.substring(5)));


        return authResponse;
    }



    public Authentication authenticateUser(String username, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    "",
                    userDetails.getAuthorities()
            );

        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User with given username does not exist");
        }
    }

}
