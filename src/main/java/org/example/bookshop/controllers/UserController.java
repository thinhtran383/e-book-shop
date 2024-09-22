package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.users.UserResponse;
import org.example.bookshop.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<PageableResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);

        Page<UserResponse> userPage = userService.getAllUsers(pageable);

        PageableResponse<UserResponse> response = PageableResponse.<UserResponse>builder()
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .elements(userPage.getContent())
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<UserResponse>>builder()
                .data(response)
                .message("Success")
                .build());


    }
}
