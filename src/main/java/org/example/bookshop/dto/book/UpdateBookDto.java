package org.example.bookshop.dto.book;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateBookDto {
    @Hidden
    private Integer id;

    private String title;
    private String author;
    private Integer quantity;
    private Integer categoryId;
    private String description;
    private String publisher;
    private BigDecimal price;
    private MultipartFile image;



}
