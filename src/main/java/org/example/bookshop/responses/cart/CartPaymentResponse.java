package org.example.bookshop.responses.cart;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartPaymentResponse {
    @JsonAlias("carts")
    private List<CartResponse> cartResponses;

    private BigDecimal totalPayment;
    private Integer totalItem;
}
