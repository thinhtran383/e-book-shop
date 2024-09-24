package org.example.bookshop.repositories;

import org.example.bookshop.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    Optional<OrderDetail> findMostRecentOrderForBookByUser(@Param("userId") int userId, @Param("bookId") int bookId);

}