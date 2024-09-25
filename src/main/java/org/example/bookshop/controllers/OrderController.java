package org.example.bookshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.order.OrderResponse;
import org.example.bookshop.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/orders")
public class OrderController {
    private final OrderService orderService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<PageableResponse<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("orderDate").descending());

        Page<OrderResponse> orderResponsePage = orderService.getAllOrders(pageRequest);

        PageableResponse<OrderResponse> response = PageableResponse.<OrderResponse>builder()
                .totalPages(orderResponsePage.getTotalPages())
                .totalElements(orderResponsePage.getTotalElements())
                .elements(orderResponsePage.getContent())
                .build();


        return ResponseEntity.ok(Response.<PageableResponse<OrderResponse>>builder()
                .data(response)
                .build()
        );
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<PageableResponse<OrderResponse>>> getAllOrdersOrderId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User userDetails
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("orderDate").descending());

        Page<OrderResponse> orderResponsePage = orderService.getAllOrdersByUserId(userDetails.getId(), pageRequest);

        PageableResponse<OrderResponse> response = PageableResponse.<OrderResponse>builder()
                .totalPages(orderResponsePage.getTotalPages())
                .totalElements(orderResponsePage.getTotalElements())
                .elements(orderResponsePage.getContent())
                .build();

        return ResponseEntity.ok(Response.<PageableResponse<OrderResponse>>builder()
                .data(response)
                .message("Orders retrieved successfully")
                .build()
        );
    }
}
