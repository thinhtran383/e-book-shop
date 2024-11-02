package org.example.bookshop.dto.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @JsonIgnore
    private String categoryName;

    private String description;

    private String publisher;

    private LocalDate publishedDate;

    private MultipartFile image;

    @JsonIgnore
    private String imageUrl;
}
