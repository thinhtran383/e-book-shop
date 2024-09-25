package org.example.bookshop.repositories;

import org.example.bookshop.entities.Order;
import org.example.bookshop.responses.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IOrderRepository extends JpaRepository<Order, Integer> {

    @Query("""
                select new org.example.bookshop.responses.order.OrderResponse(
                o.id, c.fullName, u.email, o.totalAmount, o.orderDate, o.status
                )
                from Order o
                join o.userID u
                join u.customer c
            """)
    Page<OrderResponse> findAllByOrder(Pageable pageable);

    @Query("""
                select new org.example.bookshop.responses.order.OrderResponse(
                o.id, c.fullName, u.email, o.totalAmount, o.orderDate, o.status
                )
                from Order o
                join o.userID u
                join u.customer c
                where u.id = :userId
            """)
    Page<OrderResponse> findAllByOrderByUserId(Integer userId, Pageable pageable);

}