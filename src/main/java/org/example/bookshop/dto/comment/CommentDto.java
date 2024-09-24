package org.example.bookshop.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @JsonIgnore
    private int userId;

    @JsonIgnore
    private int bookId;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Rating is required")
    private int rating;

}
