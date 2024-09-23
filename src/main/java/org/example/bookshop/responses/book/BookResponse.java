package org.example.bookshop.responses.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bookshop.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String author;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantity;

    private String categoryName;

    private String description;

    @Size(max = 255)
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    @Size(max = 255)
    private String image;

    private BigDecimal averageRating;
}
