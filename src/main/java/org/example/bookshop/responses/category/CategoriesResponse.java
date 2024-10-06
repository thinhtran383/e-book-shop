package org.example.bookshop.responses.category;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesResponse {
    private Integer id;
    private String categoryName;
    private String description;
}
