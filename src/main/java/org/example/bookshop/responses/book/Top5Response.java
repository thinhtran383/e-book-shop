package org.example.bookshop.responses.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Top5Response {
    private Integer bookID;
    private long totalBooksSold;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String author;

    private String image;
}
