package org.example.bookshop.repositories;

import org.example.bookshop.entities.ShoppingCart;
import org.example.bookshop.responses.cart.CartResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

    @Query("""
                select new org.example.bookshop.responses.cart.CartResponse(sc.id,b.id, b.title, sc.quantity, b.price)
                from ShoppingCart sc
                join sc.userID u
                join sc.bookID b
                where u.id = :userId
            """)
    List<CartResponse> findAllCartsByUserId(Integer userId);

    @Query("""
        select sc
        from ShoppingCart sc
        join sc.userID u
        join sc.bookID b
        where u.id = :userId and b.id = :bookId
    """)
    Optional<ShoppingCart> findByUserIDAndBookID(@Param("userId") Integer userId, @Param("bookId") Integer bookId);


}