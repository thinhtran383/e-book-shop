package org.example.bookshop.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.user.UpdateUserDto;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.users.UserResponse;
import org.example.bookshop.services.UserService;
import org.example.bookshop.utils.Exporter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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

    @GetMapping("/address")
    public ResponseEntity<Response<Map<String, String>>> getAddressCurrentUser(@AuthenticationPrincipal User user) {
        Map<String, String> response = Map.of(
                "address", user.getCustomer().getAddress()
        );

        return ResponseEntity.ok(Response.<Map<String, String>>builder()
                .data(response)
                .message("Success")
                .build());
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportCustomerToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.xlsx";
        response.setHeader(headerKey, headerValue);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        Page<UserResponse> userPage = userService.getAllUsers(pageable);

        Exporter<UserResponse> exporter = new Exporter<>(userPage.getContent(), "Users");

        exporter.export(response);

    }

    @GetMapping("/information")
    @PreAuthorize("#user.id == authentication.principal.id")
    public ResponseEntity<Response<UserResponse>> getUserDetails(
            @AuthenticationPrincipal User user
    ) {
        UserResponse userResponse = userService.getUserDetailsById(user.getId());

        return ResponseEntity.ok(Response.<UserResponse>builder()
                .data(userResponse)
                .message("Success")
                .build());
    }


    @PutMapping("/update-information")
    @PreAuthorize("#user.id == authentication.principal.id")
    public ResponseEntity<Response<UserResponse>> updateUserDetails(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        UserResponse updatedUser = userService.updateUserDetails(user.getId(), updateUserDto);

        return ResponseEntity.ok(Response.<UserResponse>builder()
                .data(updatedUser)
                .message("User information updated successfully")
                .build());
    }
}
