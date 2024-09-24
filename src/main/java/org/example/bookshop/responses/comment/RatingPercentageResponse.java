package org.example.bookshop.responses.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingPercentageResponse {
    private Integer rating;
    private Long count;
    private Double percentage;

    public RatingPercentageResponse(Integer rating, Long count) {
        this.rating = rating;
        this.count = count;
    }
}
