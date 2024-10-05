package org.example.bookshop.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStatusDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotBlank(message = "Status is required")
    private String status;

    private String note;
}
