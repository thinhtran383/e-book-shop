package org.example.bookshop.responses.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Integer id;
    private String customerName;
    private String email;
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate orderDate;
    private String status;
}
