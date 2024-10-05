package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.entities.Order;
import org.example.bookshop.entities.ShoppingCart;
import org.example.bookshop.repositories.IOrderDetailRepository;
import org.example.bookshop.repositories.IOrderRepository;
import org.example.bookshop.repositories.IShoppingCartRepository;
import org.example.bookshop.responses.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final IShoppingCartRepository shoppingCartRepository;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable, String status) {
        Page<OrderResponse> orderResponsePage = orderRepository.findAllByOrder(pageable);

        if (status != null) {
            List<OrderResponse> filteredOrders = orderResponsePage.getContent()
                    .stream().
                    filter(orderResponse -> orderResponse.getStatus().equals(status))
                    .toList();
            return new PageImpl<>(filteredOrders, pageable, filteredOrders.size());
        }

        return orderResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersByUserId(Integer userId, Pageable pageable) {
        Page<OrderResponse> orderResponsePage = orderRepository.findAllByOrderByUserId(userId, pageable);
        return orderResponsePage;
    }

    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, String status, String note) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        order.setNote(note);
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

}
