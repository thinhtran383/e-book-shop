package org.example.bookshop.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response <T> {
    private T data;
    private String message;
}
