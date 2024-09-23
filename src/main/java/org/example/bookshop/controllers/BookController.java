package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.BookDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.services.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Response<PageableResponse<BookResponse>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, limit);

        org.springframework.data.domain.Page<BookResponse> bookPage = bookService.getAllBooks(pageable);

        PageableResponse<BookResponse> response = PageableResponse.<BookResponse>builder()
                .totalPages(bookPage.getTotalPages())
                .totalElements(bookPage.getTotalElements())
                .elements(bookPage.getContent())
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<BookResponse>>builder()
                .data(response)
                .message("Success")
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<BookResponse>> createNewBook(
            @RequestBody BookDto bookDto
    ) {
        return ResponseEntity.ok(Response.<BookResponse>builder()
                .data(bookService.createNewBooK(bookDto))
                .message("Success")
                .build());
    }
}
