package org.example.bookshop.repositories;

import org.example.bookshop.entities.Comment;
import org.example.bookshop.responses.comment.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    @Query("""
        select new org.example.bookshop.responses.comment.CommentResponse(
            c.content,
            cu.fullName,
            c.commentDate,
            r.rating
          
            )
        from Comment c
        join c.bookID b
        join c.userID u
        join u.customer cu
        left join Rating r on r.bookID = b  
        where b.id = :bookID
        """)
    Page<CommentResponse> findAllCommentByBookID(Integer bookID, Pageable pageable);


    @Query("SELECT c FROM Comment c WHERE c.userID.id = :userId AND c.bookID.id = :bookId ORDER BY c.commentDate DESC limit 1")
    Optional<Comment> findMostRecentCommentByUserAndBook(@Param("userId") int userId, @Param("bookId") int bookId);


}