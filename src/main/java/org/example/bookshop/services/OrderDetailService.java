package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.database.IOrderDetailRepository;
import org.example.bookshop.responses.order.OrderDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final IOrderDetailRepository orderDetailRepository;

    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrderDetailsByUserId(Integer userId, Integer orderId) {
        List<OrderDetailsResponse> orderDetailsResponses = orderDetailRepository.findOrderDetailsByUserId(userId, orderId);

        if (orderDetailsResponses.isEmpty()) {
            throw new DataNotFoundException("Order not found");
        } else {
            return orderDetailsResponses;
        }
    }

    @Transactional
    public List<OrderDetailsResponse> getAllOrderDetailsByOrderId(Integer orderId) {
        List<OrderDetailsResponse> orderDetailsResponses = orderDetailRepository.findOrderDetailsByOrderId(orderId);

        if (orderDetailsResponses.isEmpty()) {
            throw new DataNotFoundException("Order not found");
        } else {
            return orderDetailsResponses;
        }
    }
}
