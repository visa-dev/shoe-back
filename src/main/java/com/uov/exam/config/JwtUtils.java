package com.uov.exam.config;

import com.uov.exam.Exception.JwtAuthenticationException;
import com.uov.exam.model.User;
import com.uov.exam.repo.UserRepo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;  // Changed to long for better handling of time

    @Autowired
    private UserRepo userRepo;

    // Generate JWT Token from Authentication object
    public String generateJwtToken(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);

        // Generate the JWT token
        return Jwts.builder()
                .setSubject(authentication.getName())// Set the username as the subject
                .claim("roles",roles ) // Set roles as a claim
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue date to current time
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Set expiration time
                .signWith(SignatureAlgorithm.HS512, secretKey) // Sign the token using the secret key
                .compact(); // Build the token
    }

    // Extract JWT from the request header
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove "Bearer " prefix
        }

        return null;
    }

    // Extract claims from the token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Populate authorities and convert them to a comma-separated string
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public User getCurrentUserByJwtToken(String jwtToken) {
        try {
            Claims claims = parseJwt(jwtToken);
            String username = claims.getSubject();

            return getUserFromDatabase(username);

        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("JWT token is expired");
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new JwtAuthenticationException("Malformed JWT token");
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to parse JWT token");
        }
    }

    private Claims parseJwt(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private User getUserFromDatabase(String username) {
        return userRepo.getUserByUsername(username);
    }

    // Extract username (email) from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract roles from the token
    public String extractRoles(String token) {
        return (String) extractClaims(token).get("roles"); // Changed to "roles" for consistency
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new JwtAuthenticationException("Your session has expired. Please log in again to continue");

        }

    }

    // Validate the token by checking if it's expired
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
