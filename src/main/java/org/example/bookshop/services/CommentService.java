package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.repositories.ICommentRepository;
import org.example.bookshop.responses.comment.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ICommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByBookId(Integer bookId, Pageable pageable){
        Page<CommentResponse> commentResponses = commentRepository.findAllCommentByBookID(bookId, pageable);

        return commentResponses;
    }
}
