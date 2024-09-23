package org.example.bookshop.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private String title;
    private String author;
    private BigDecimal price;
    private Integer quantity;
    private Integer categoryID;
    private String categoryName;
    private String description;
    private String publisher;
    private LocalDate publishedDate;
    private String image;
}
