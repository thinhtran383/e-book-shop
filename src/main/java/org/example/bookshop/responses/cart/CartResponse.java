package org.example.bookshop.responses.cart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Integer cartId;
    private Integer bookId;
    private String title;
    private int quantity;
    private BigDecimal price;
    private BigDecimal rowTotal;
    private String imageUrl;

    public CartResponse(Integer cartId, Integer bookId, String title, int quantity, BigDecimal price, String imageUrl) {
        this.cartId = cartId;
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
