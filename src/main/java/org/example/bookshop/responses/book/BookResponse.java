package org.example.bookshop.responses.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    @Size(max = 255)
    private String image;

    private BigDecimal averageRating;

    private Long purchaseCount;
    private Integer categoryId;

    public BookResponse(Integer id, String title, String author, BigDecimal price,
                        Integer quantity, String categoryName, String description,
                        String publisher, LocalDate publishedDate, String image, BigDecimal averageRating, Integer categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.image = image;
        this.averageRating = averageRating;
        this.categoryId = categoryId;
    }
}
