package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.order.CancelOrderDto;
import org.example.bookshop.dto.order.ConfirmOrder;
import org.example.bookshop.dto.order.UpdateOrderStatusDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/orders")
public class OrderController {
    private final OrderService orderService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<PageableResponse<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String status
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("orderDate").descending());

        Page<OrderResponse> orderResponsePage = orderService.getAllOrders(pageRequest, status);

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


    @Hidden
    @PutMapping("/update-status-order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<OrderResponse>> updateOrderStatus(
            @RequestBody UpdateOrderStatusDto updateOrderStatusDto
    ) {
        OrderResponse orderResponse = orderService.updateOrderStatus(updateOrderStatusDto.getOrderId(), updateOrderStatusDto.getStatus(), updateOrderStatusDto.getNote());

        return ResponseEntity.ok(Response.<OrderResponse>builder()
                .data(orderResponse)
                .message("Order status updated successfully")
                .build()
        );
    }

    @PutMapping("/confirm/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Response<OrderResponse>> confirmOrder(
            @RequestBody ConfirmOrder note,
            @PathVariable Integer orderId
    ) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, "Completed", note.getNote());

        return ResponseEntity.ok(Response.<OrderResponse>builder()
                .data(orderResponse)
                .message("Order confirmed successfully")
                .build()
        );
    }

    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<OrderResponse>> cancelOrder(
            @RequestBody ConfirmOrder cancelOrder,
            @PathVariable Integer orderId

    ) {
        OrderResponse orderResponse = orderService.cancelOrder(orderId, cancelOrder.getNote());

        return ResponseEntity.ok(Response.<OrderResponse>builder()
                .data(orderResponse)
                .message("Order cancelled successfully")
                .build()
        );
    }


}
