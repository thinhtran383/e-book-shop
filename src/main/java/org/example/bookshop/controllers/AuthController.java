package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.user.ChangePassDto;
import org.example.bookshop.dto.user.LoginDto;
import org.example.bookshop.dto.user.RegisterDto;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.users.LoginResponse;
import org.example.bookshop.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @Hidden
    @PostMapping("/currentUser")
    public ResponseEntity<String> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @PostMapping("/change-password")
    public ResponseEntity<Response<LoginResponse>> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePassDto changePassDto
    ) {
        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .data(authService.changePassword(changePassDto, user.getId()))
                .message("Password changed successfully")
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(
            @RequestParam @Email @Valid String email
    ) {
        authService.forgotPassword(email);

        return ResponseEntity.ok(Response.builder()
                .message("Please check your email for more information")
                .build());
    }


}
