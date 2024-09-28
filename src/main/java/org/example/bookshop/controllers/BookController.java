package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.book.BookDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.services.BookService;
import org.example.bookshop.services.CloudinaryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.example.bookshop.utils.Exporter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/books")
@Slf4j
public class BookController {

    private final BookService bookService;
    private final CloudinaryService cloudinaryService;


    @Hidden
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public Mono<ResponseEntity<Response<String>>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {


        return cloudinaryService.uploadFile(file)
                .map(uploadResult -> ResponseEntity.ok(Response.<String>builder()
                        .data(null)
                        .message("Upload image success")
                        .build()));

    }

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

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Response<Mono<BookResponse>>> createNewBook(
            @ModelAttribute BookDto bookDto
    ) {
        bookService.createNewBook(bookDto);


        return ResponseEntity.ok(Response.<Mono<BookResponse>>builder()
                .data(null)
                .message("Success")
                .build());
    }

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
    ResponseEntity<Response<PageableResponse<BookResponse>>> filterBooks(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String sort
    ) {
        Sort sorting = Sort.unsorted();

        if (sort != null) {
            if (sort.equalsIgnoreCase("asc")) {
                sorting = Sort.by(Sort.Direction.ASC, "price");
            } else if (sort.equalsIgnoreCase("desc")) {
                sorting = Sort.by(Sort.Direction.DESC, "price");
            }
        }

        Pageable pageable = PageRequest.of(page, limit, sorting);

        Page<BookResponse> bookResponses = bookService.filterBooks(category, priceMin, priceMax, pageable);

        PageableResponse<BookResponse> response = PageableResponse.<BookResponse>builder()
                .totalPages(bookResponses.getTotalPages())
                .totalElements(bookResponses.getTotalElements())
                .elements(bookResponses.getContent())
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<BookResponse>>builder()
                .data(response)
                .message("Success")
                .build()
        );
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categories.xlsx";
        response.setHeader(headerKey, headerValue);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        Page<BookResponse> bookResponses = bookService.getAllBooks(pageable);

        Exporter<BookResponse> exporter = new Exporter<>(bookResponses.getContent(), "Books");

        exporter.export(response);

    }
}
