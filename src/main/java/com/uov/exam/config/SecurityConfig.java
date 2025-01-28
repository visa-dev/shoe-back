package com.uov.exam.config;


import com.uov.exam.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;  // Your JwtFilter for handling JWT authentication


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection for stateless authentication (JWT)

				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.antMatchers("/api/auth/login", "/api/auth/register", "/api/auth/validate-token").permitAll() // Allow login, register, and validate-token publicly
								.antMatchers("/api/auth/order/**").hasAnyRole("CUSTOMER","MANAGER") // Protect profile routes for students
								.anyRequest().authenticated()
				)
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors-> cors.configurationSource(corsConfigrationSource()))

				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless session for JWT
				)

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter before the basic auth filter

				.exceptionHandling(exceptionHandling ->exceptionHandling
						.authenticationEntryPoint(((request, response, authException) ->
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())
						))
				)
				.build();


	}

	private CorsConfigurationSource corsConfigrationSource() {

		return request -> {

            CorsConfiguration cfg=new CorsConfiguration();

            cfg.setAllowedOrigins(List.of(
                    "http://localhost:3000/","http://127.0.0.1:3000/"
            ));

            cfg.setAllowedMethods(Collections.singletonList("*"));
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            cfg.setExposedHeaders(List.of("Authorization"));
            cfg.setMaxAge(3600L);

            return cfg;
        };


	}




	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder());

		return authenticationManagerBuilder.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService(); // Implement CustomUserDetailsService to load user data
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
