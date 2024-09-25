package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.mail.MailDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @PostMapping("/send-mail")
    public ResponseEntity<Response<String>> sendMail(
            @RequestBody MailDto mailDto
    ) {
        mailService.sendMail(mailDto.getTo(), mailDto);

         return ResponseEntity.ok(Response.<String>builder()
                .data("Send mail to `" + mailDto.getTo() + "`")
                .message("Success")
                .build());
    }
}
