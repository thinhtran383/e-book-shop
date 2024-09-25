package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.entities.Order;
import org.example.bookshop.repositories.IOrderDetailRepository;
import org.example.bookshop.repositories.IOrderRepository;
import org.example.bookshop.responses.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<OrderResponse> orderResponsePage = orderRepository.findAllByOrder(pageable);
        return orderResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersByUserId(Integer userId, Pageable pageable) {
        Page<OrderResponse> orderResponsePage = orderRepository.findAllByOrderByUserId(userId, pageable);
        return orderResponsePage;
    }
}
