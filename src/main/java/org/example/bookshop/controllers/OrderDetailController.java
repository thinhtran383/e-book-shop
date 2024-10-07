package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.order.OrderDetailsResponse;
import org.example.bookshop.services.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/user/{orderId}")
    public ResponseEntity<Response<List<OrderDetailsResponse>>> getAllOrderDetailsByOrderId(
            @PathVariable Integer orderId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(Response.<List<OrderDetailsResponse>>builder()
                .data(orderDetailService.getAllOrderDetailsByUserId(user.getId(), orderId))
                .message("Success")
                .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<Response<List<OrderDetailsResponse>>> getAllOrderDetailsByOrderId(
            @PathVariable Integer orderId
    ) {
        return ResponseEntity.ok(Response.<List<OrderDetailsResponse>>builder()
                .data(orderDetailService.getAllOrderDetailsByOrderId(orderId))
                .message("Success")
                .build()
        );
    }
}
