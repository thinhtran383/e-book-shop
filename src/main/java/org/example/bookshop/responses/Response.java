package org.example.bookshop.responses;


import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Response <T> {
    private T data;
    private String message;
}
