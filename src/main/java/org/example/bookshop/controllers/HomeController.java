package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {


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
}
