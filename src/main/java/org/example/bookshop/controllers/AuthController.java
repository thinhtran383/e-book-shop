package org.example.bookshop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.user.LoginDto;
import org.example.bookshop.dto.user.RegisterDto;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.users.LoginResponse;
import org.example.bookshop.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Response<RegisterDto>> register(
            @RequestBody @Valid RegisterDto registerDto
    ) {
        return ResponseEntity.ok(Response.<RegisterDto>builder()
                .data(authService.register(registerDto))
                .message("User registered successfully")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(
            @RequestBody @Valid LoginDto loginDto
    ) {
        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .data(authService.login(loginDto))
                .message("Customer logged in successfully")
                .build());
    }

    @PostMapping("/admin")
    public ResponseEntity<Response<LoginResponse>> adminLogin(
            @RequestBody @Valid LoginDto loginDto
    ) {
        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .data(authService.adminLogin(loginDto))
                .message("Admin logged in successfully")
                .build());
    }

    @PostMapping("/currentUser")
    public ResponseEntity<String> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsername());
    }


}
