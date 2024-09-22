package org.example.bookshop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.RegisterDto;
import org.example.bookshop.responses.Response;
import org.example.bookshop.services.AuthService;
import org.example.bookshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
