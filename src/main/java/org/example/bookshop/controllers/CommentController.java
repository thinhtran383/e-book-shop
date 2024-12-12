package org.example.bookshop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.dto.comment.CommentDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.comment.CommentResponse;
import org.example.bookshop.services.BookService;
import org.example.bookshop.services.CommentService;
import org.example.bookshop.components.JwtGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final BookService bookService;
    private final JwtGenerator jwtGenerator;


    @GetMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> getCommentAndRatingByBookId(
            @PathVariable Integer bookId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            HttpServletRequest request
    ) {
        int userId = 0;

        if (request.getHeader("Authorization") != null) {
            String bearerToken = request.getHeader("Authorization").substring(7);
            userId = jwtGenerator.extractUserId(bearerToken);
        }


        PageableResponse<CommentResponse> pageableResponse = commentService.getCommentByBookId(bookId, page, limit);

        BigDecimal averageRating = bookService.getAverageRatingById(bookId);

        boolean enableComment = commentService.isEnableComment(bookId, userId) && userId != 0;

        Map<String, Object> response = Map.of(
                "data", pageableResponse,
                "averageRating", averageRating,
                "enableComment", enableComment,
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
