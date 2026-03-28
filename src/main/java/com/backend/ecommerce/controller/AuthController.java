package com.backend.ecommerce.controller;

import com.backend.ecommerce.dto.LoginDto;
import com.backend.ecommerce.dto.RegisterDto;
import com.backend.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 1. Register new Customer
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {

        try {
            authService.register(registerDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully!");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }

    }

    // 2. Login Customer
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {

        try {

            String token = authService.login(loginDto);
            return ResponseEntity.ok()
                    .body("token: "+ token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }


}