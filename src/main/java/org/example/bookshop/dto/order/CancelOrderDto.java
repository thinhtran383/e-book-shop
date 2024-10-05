package org.example.bookshop.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;
    
    @JsonIgnore
    private String status;

    private String note;
}
