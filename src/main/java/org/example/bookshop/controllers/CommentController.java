package org.example.bookshop.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.comment.CommentResponse;
import org.example.bookshop.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{bookId}")
    public ResponseEntity<Response<PageableResponse<CommentResponse>>> getCommentAndRatingByBookId(
            @PathVariable Integer bookId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);

        Page<CommentResponse> commentResponses = commentService.getCommentByBookId(bookId, pageable);

        return ResponseEntity.ok(Response.<PageableResponse<CommentResponse>>builder()
                .data(PageableResponse.<CommentResponse>builder()
                        .totalPages(commentResponses.getTotalPages())
                        .totalElements(commentResponses.getTotalElements())
                        .elements(commentResponses.getContent())
                        .build()
                )
                .message("Get comment and rating by book id success")
                .build());
    }
}
