package org.example.bookshop.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> extends Response<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
}
