package org.example.bookshop.responses.category;

import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoriesResponse {
    private Integer id;
    private String categoryName;
    private String description;
}
