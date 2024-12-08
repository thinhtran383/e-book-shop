package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.book.BookDto;
import org.example.bookshop.dto.book.UpdateBookDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.responses.book.PublisherResponse;
import org.example.bookshop.services.BookService;
import org.example.bookshop.utils.Exporter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/books")
@Slf4j
public class BookController {
    private final BookService bookService;

    @GetMapping("/details/{bookID}")
    public ResponseEntity<Response<BookResponse>> getBookByID(
            @PathVariable int bookID
    ) {
        return ResponseEntity.ok(Response.<BookResponse>builder()
                .data(bookService.getBookById(bookID))
                .message("Success")
                .build()
        );
    }

//    @GetMapping
//    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Response<PageableResponse<BookResponse>>> getAllBooks(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int limit
//
//            ) {
//        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, limit);
//
//        org.springframework.data.domain.Page<BookResponse> bookPage = bookService.getAllBooks(pageable);
//
//        PageableResponse<BookResponse> response = PageableResponse.<BookResponse>builder()
//                .totalPages(bookPage.getTotalPages())
//                .totalElements(bookPage.getTotalElements())
//                .elements(bookPage.getContent())
//                .build();
//
//
//        return ResponseEntity.ok(Response.<PageableResponse<BookResponse>>builder()
//                .data(response)
//                .message("Success")
//                .build());
//    }

//    @PostMapping(consumes = "multipart/form-data")
//    public ResponseEntity<Response<Mono<BookResponse>>> createNewBook(
//            @ModelAttribute BookDto bookDto
//    ) {
//        bookService.createNewBook(bookDto);
//
//
//        return ResponseEntity.ok(Response.<Mono<BookResponse>>builder()
//                .data(null)
//                .message("Success")
//                .build());
//    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Response<BookResponse>> createNewBook(
            @ModelAttribute BookDto bookDto
    ) {
        return ResponseEntity.ok(Response.<BookResponse>builder()
                .data(bookService.createNewBook(bookDto))
                .message("Success")
                .build());
    }


    @Hidden
    @GetMapping("/{categoryID}")
    public ResponseEntity<Response<PageableResponse<BookResponse>>> getBooksByCategory(
            @PathVariable int categoryID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, limit);


        org.springframework.data.domain.Page<BookResponse> bookPage = bookService.getBooksByCategory(categoryID, pageable);

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

    @DeleteMapping("/{bookID}")
    public ResponseEntity<Response<BookResponse>> deleteBook(
            @PathVariable int bookID
    ) {
        return ResponseEntity.ok(Response.<BookResponse>builder()
                .data(bookService.deleteBook(bookID))
                .message("Delete book success")
                .build());
    }

    @GetMapping()
    public ResponseEntity<Response<PageableResponse<BookResponse>>> filterBooks(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String sort
    ) {




        PageableResponse<BookResponse> response = bookService.filterBooks(category, priceMin,
                priceMax, publisher, title, page, limit);

        return ResponseEntity.ok(Response.<PageableResponse<BookResponse>>builder()
                .data(response)
                .message("Success")
                .build()
        );
    }


    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categories.xlsx";
        response.setHeader(headerKey, headerValue);

        PageableResponse<BookResponse> bookResponses = bookService.getAllBooks(0, Integer.MAX_VALUE);

        Exporter<BookResponse> exporter = new Exporter<>(bookResponses.getElements(), "Books");

        exporter.export(response);

    }

    @PutMapping(value = "/update-book/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<Response<BookResponse>> updateBook(@ModelAttribute UpdateBookDto updateBookDto, @PathVariable("book-id") int bookID) {
        updateBookDto.setId(bookID);

        return ResponseEntity.ok(Response.<BookResponse>builder()
                .data(bookService.updateBook(updateBookDto))
                .message("Update book success")
                .build());
    }

    @GetMapping("/publishers")
    public ResponseEntity<Response<PageableResponse<PublisherResponse>>> getAllPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {


        Pageable pageable = PageRequest.of(page, limit);

        Page<PublisherResponse> publisherResponses = bookService.getPublishers(pageable);

        PageableResponse<PublisherResponse> response = PageableResponse.<PublisherResponse>builder()
                .totalPages(publisherResponses.getTotalPages())
                .totalElements(publisherResponses.getTotalElements())
                .elements(publisherResponses.getContent())
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<PublisherResponse>>builder()
                .data(response)
                .message("Success")
                .build());
    }
}
