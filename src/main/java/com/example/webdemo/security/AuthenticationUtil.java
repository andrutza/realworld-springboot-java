package com.example.webdemo.security;

import com.example.webdemo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthenticationUtil {

    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;

    public AuthenticationUtil(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("password", user.getPassword())
                .claim("username", user.getUsername())
                .claim("bio", user.getBio())
                .claim("image", user.getImage())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET.getBytes())
                .compact();
    }
}
