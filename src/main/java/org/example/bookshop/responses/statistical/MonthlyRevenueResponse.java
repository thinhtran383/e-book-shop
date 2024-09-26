package org.example.bookshop.responses.statistical;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyRevenueResponse {
    private Integer month;
    private Integer year;
    private BigDecimal totalRevenue;
}