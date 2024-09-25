package org.example.bookshop.repositories;

import org.example.bookshop.entities.OrderDetail;
import org.example.bookshop.responses.order.OrderDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {


    @Query("""
                select count(od)
                from OrderDetail od
                join od.orderID o
                join o.userID u
                where u.id = :userId and od.bookID.id = :bookId and o.status = 'COMPLETED'
            """)
    long countBooksPurchasedByUser(Integer userId, Integer bookId);


    @Query("SELECT od FROM OrderDetail od join od.orderID o WHERE o.userID.id = :userId AND od.bookID.id = :bookId ORDER BY od.orderID.orderDate DESC limit 1")
    Optional<OrderDetail> findMostRecentOrderForBookByUser(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Query("""
            SELECT new org.example.bookshop.responses.order.OrderDetailsResponse(o.id, b.title, od.quantity, od.price)
            from OrderDetail od
            join od.bookID b
            join od.orderID o
            where o.userID.id = :userId and o.id = :orderId
            """)
    List<OrderDetailsResponse> findOrderDetailsByUserId(@Param("userId") Integer userId, @Param("orderId") Integer orderId);
}