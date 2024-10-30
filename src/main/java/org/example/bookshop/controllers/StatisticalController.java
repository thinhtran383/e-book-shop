package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.book.Top5Response;
import org.example.bookshop.responses.statistical.MonthlyRevenueResponse;
import org.example.bookshop.services.StatisticalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/statistical")
@RequiredArgsConstructor
public class StatisticalController {
    private final StatisticalService statisticalService;

    @GetMapping("/top5-books")
    public ResponseEntity<Response<List<Top5Response>>> getTop5BooksBySales() {
        return ResponseEntity.ok(Response.<List<Top5Response>>builder()
                .data(statisticalService.getTop5BooksBySales())
                .message("Success")
                .build());
    }

    @GetMapping("/revenue-by-month")
    public ResponseEntity<Response<List<MonthlyRevenueResponse>>> getRevenueByMonth() {
        return ResponseEntity.ok(Response.<List<MonthlyRevenueResponse>>builder()
                .data(statisticalService.getRevenueByMonth())
                .message("Success")
                .build());
    }

    @GetMapping("/total-customers")
    public ResponseEntity<Response<Long>> getTotalCustomers() {
        return ResponseEntity.ok(Response.<Long>builder()
                .data(statisticalService.getTotalCustomers())
                .message("Success")
                .build());
    }

    @GetMapping("/total-orders")
    public ResponseEntity<Response<Long>> getTotalOrders() {
        return ResponseEntity.ok(Response.<Long>builder()
                .data(statisticalService.getTotalOrders())
                .message("Success")
                .build());
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<Response<String>> getTotalRevenue() {
        return ResponseEntity.ok(Response.<String>builder()
                .data(statisticalService.getTotalRevenue().toString())
                .message("Success")
                .build());
    }
}
