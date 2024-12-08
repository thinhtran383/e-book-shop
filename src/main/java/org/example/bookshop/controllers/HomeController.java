package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.services.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final MailService mailService;

    @GetMapping
    public ResponseEntity<Response<PageableResponse<String>>> home() {
        PageableResponse<String> response = PageableResponse.<String>builder()
                .totalPages(1)
                .totalElements(2)
                .elements(List.of("Hello", "World"))
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<String>>builder()
                .data(response)
                .message("Success")
                .build());
    }


    @GetMapping("/user")
    public ResponseEntity<Response> userCredentials(Principal principal) {
        return ResponseEntity.ok(Response.builder()
                .message(principal.getName())
                .build());
    }

}
