package org.example.bookshop.repositories;

import org.example.bookshop.entities.Comment;
import org.example.bookshop.responses.comment.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    @Query("""
        select new org.example.bookshop.responses.comment.CommentResponse(
            c.content,
            cu.fullName,
            c.commentDate,
            r.rating,
            b.averageRating
            )
        from Comment c
        join c.bookID b
        join c.userID u
        join u.customer cu
        left join Rating r on r.bookID = b  
        where b.id = :bookID
        """)
    Page<CommentResponse> findAllCommentByBookID(Integer bookID, Pageable pageable);

}