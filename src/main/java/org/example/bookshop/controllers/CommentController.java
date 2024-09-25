package org.example.bookshop.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.comment.CommentDto;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.comment.CommentResponse;
import org.example.bookshop.services.BookService;
import org.example.bookshop.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/comments")
public class CommentController {
    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> getCommentAndRatingByBookId(
            @PathVariable Integer bookId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("commentDate").descending());

        Page<CommentResponse> commentResponses = commentService.getCommentByBookId(bookId, pageable);

        PageableResponse<CommentResponse> pageableResponse = PageableResponse.<CommentResponse>builder()
                .totalPages(commentResponses.getTotalPages())
                .totalElements(commentResponses.getTotalElements())
                .elements(commentResponses.getContent())
                .build();

        BigDecimal averageRating = bookService.getAverageRatingById(bookId);


        Map<String, Object> response = Map.of(
                "data", pageableResponse,
                "averageRating", averageRating,
                "detailRating", commentService.calculateRatingPercentage(bookId),
                "message", "Get comment and rating by book id success");

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{bookId}")
    public ResponseEntity<Response<Map<String, Object>>> createComment(
            @PathVariable Integer bookId,
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal User userDetails
    ) {
        commentDto.setUserId(userDetails.getId());
        commentDto.setBookId(bookId);

        return ResponseEntity.ok(Response.<Map<String, Object>>builder()
                .data(commentService.saveComment(commentDto))
                .message("Create comment success")
                .build());
    }
}