package org.example.bookshop.dto.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderDto {
    private String note;
}
