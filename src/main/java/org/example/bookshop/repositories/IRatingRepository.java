package org.example.bookshop.repositories;

import org.example.bookshop.entities.Rating;
import org.example.bookshop.responses.comment.RatingPercentageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT new org.example.bookshop.responses.comment.RatingPercentageResponse(r.rating, COUNT(r)) " +
            "FROM Rating r WHERE r.bookID.id = :bookID " +
            "GROUP BY r.rating")
    List<RatingPercentageResponse> countRatingByBookID(@Param("bookID") Integer bookID);

    long countByBookID_Id(Integer bookID);

}