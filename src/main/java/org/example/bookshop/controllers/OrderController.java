package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.order.ConfirmOrder;
import org.example.bookshop.dto.order.UpdateOrderStatusDto;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.order.OrderResponse;
import org.example.bookshop.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        PageableResponse<OrderResponse> response = orderService.getAllOrders(page, limit, status);

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

        PageableResponse<OrderResponse> response = orderService.getAllOrdersByUserId(userDetails.getId(), page, limit);

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

    @PutMapping("/admin/confirm/{orderId}")
// @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Response<OrderResponse>> confirmOrder(
            @RequestBody(required = false) ConfirmOrder note,
            @PathVariable Integer orderId
    ) {
        String noteContent = (note != null && note.getNote() != null) ? note.getNote() : null;

        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, "Delivering", noteContent);

        return ResponseEntity.ok(Response.<OrderResponse>builder()
                .data(orderResponse)
                .message("Order confirmed successfully")
                .build()
        );
    }


    @PutMapping("/user/received/{orderId}")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Response<OrderResponse>> receivedOrder(
            @RequestBody(required = false) ConfirmOrder note,
            @PathVariable Integer orderId
    ) {

        String noteContent = (note != null && note.getNote() != null) ? note.getNote() : null;
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, "Completed", noteContent);

        return ResponseEntity.ok(Response.<OrderResponse>builder()
                .data(orderResponse)
                .message("Order confirmed successfully")
                .build()
        );
    }

    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response<OrderResponse>> cancelOrder(
            @RequestBody(required = false) ConfirmOrder cancelOrder,
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
