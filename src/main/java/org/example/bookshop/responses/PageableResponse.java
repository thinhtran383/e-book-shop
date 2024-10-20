package org.example.bookshop.responses;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class PageableResponse<T> {
    private List<T> elements;
    private int totalPages;
    private long totalElements;
}
