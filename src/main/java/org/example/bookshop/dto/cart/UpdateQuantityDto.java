package org.example.bookshop.dto.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuantityDto {
//    private Integer cartId;
    private Integer bookId;
    private Integer newQuantity;
}
