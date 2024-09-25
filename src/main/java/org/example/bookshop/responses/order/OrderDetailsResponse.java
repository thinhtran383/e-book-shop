package org.example.bookshop.responses.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsResponse {
    private int orderId;
    private String bookName;
    private int quantity;
    private BigDecimal pricePerBook;
}
