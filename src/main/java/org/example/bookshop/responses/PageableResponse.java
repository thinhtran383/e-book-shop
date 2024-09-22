package org.example.bookshop.responses;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PageableResponse<T>{
    private List<T> elements;
    private int totalPages;
    private long totalElements;
}
