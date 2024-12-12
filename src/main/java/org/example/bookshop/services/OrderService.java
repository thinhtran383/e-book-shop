package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.domain.database.Order;
import org.example.bookshop.exceptions.ResourceAlreadyExisted;
import org.example.bookshop.repositories.database.IOrderRepository;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.order.OrderResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final IOrderRepository orderRepository;
    private final BookService bookService;

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "#page + '-' + #status")
    public PageableResponse<OrderResponse> getAllOrders(int page, int limit, String status) {
        Page<OrderResponse> orderResponsePage = orderRepository.findAllByOrder(PageRequest.of(page, limit, Sort.by("orderDate").descending()));

        if (status != null) {
            List<OrderResponse> filteredOrders = orderResponsePage.getContent()
                    .stream().
                    filter(orderResponse -> orderResponse.getStatus().equals(status))
                    .toList();

            return PageableResponse.<OrderResponse>builder()
                    .elements(filteredOrders)
                    .totalElements(filteredOrders.size())
                    .totalPages(filteredOrders.size() / limit)
                    .build();
        }

        return PageableResponse.<OrderResponse>builder()
                .elements(orderResponsePage.getContent())
                .totalElements(orderResponsePage.getTotalElements())
                .totalPages(orderResponsePage.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public PageableResponse<OrderResponse> getAllOrdersByUserId(Integer userId, int page, int limit) {
        Page<OrderResponse> orderResponses = orderRepository.findAllByOrderByUserId(userId,
                PageRequest.of(page, limit, Sort.by("orderDate").descending())
        );

        return PageableResponse.<OrderResponse>builder()
                .elements(orderResponses.getContent())
                .totalElements(orderResponses.getTotalElements())
                .totalPages(orderResponses.getTotalPages())
                .build();
    }

    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, String status, String note) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        log.error("Order status: {}", order.getStatus());

        order.setStatus(status);

        if (note != null) {
            order.setNote(note);
        }

        orderRepository.save(order);
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getUserID().getCustomer().getFullName())
                .email(order.getUserID().getEmail())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    @Transactional // todo: optimize this
    public OrderResponse cancelOrder(Integer orderId, String note) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        switch (order.getStatus()) {
            case "Delivering" -> throw new ResourceAlreadyExisted("Cannot cancel delivering order");
            case "CANCELLED" -> throw new ResourceAlreadyExisted("Order already cancelled");
            case "Completed" -> throw new ResourceAlreadyExisted("Cannot cancel completed order");
        }

        order.getOrderDetails().forEach(orderDetail -> increaseBookQuantity(orderDetail.getBookID().getId(), orderDetail.getQuantity()));

        order.setStatus("CANCELLED");

        if (note != null) {
            order.setNote(note);
        }

        orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getUserID().getCustomer().getFullName())
                .email(order.getUserID().getEmail())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    private void increaseBookQuantity(Integer bookId, Integer quantity) {
        bookService.incQuantity(bookId, quantity);
    }
}
