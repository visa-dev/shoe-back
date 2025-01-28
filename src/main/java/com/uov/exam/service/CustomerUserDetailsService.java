package com.uov.exam.service;

import com.uov.exam.model.USER_ROLE;
import com.uov.exam.model.User;
import com.uov.exam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user= userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with username "+ username);
        }

        USER_ROLE role=user.get().getRole();

        GrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_"+role.name());

        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), Collections.singletonList(authorities));
    }



}
