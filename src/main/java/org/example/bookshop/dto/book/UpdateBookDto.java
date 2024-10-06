package org.example.bookshop.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateBookDto {
    @NotBlank(message = "Id is required")
    private Integer id;
    private String title;
    private String author;
    private Integer quantity;
    private Integer categoryId;
    private String description;
    private String publisher;
    private MultipartFile image;



}
