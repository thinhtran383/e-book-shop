package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.cart.UpdateQuantityDto;
import org.example.bookshop.entities.*;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.IOrderDetailRepository;
import org.example.bookshop.repositories.IOrderRepository;
import org.example.bookshop.repositories.IShoppingCartRepository;
import org.example.bookshop.responses.cart.CartPaymentResponse;
import org.example.bookshop.responses.cart.CartResponse;
import org.example.bookshop.responses.order.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {
    private final IShoppingCartRepository shoppingCartRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final BookService bookService;

    @Transactional(readOnly = true)
    public CartPaymentResponse getCartsByUserId(Integer userId) {
        return calculateRowTotal(userId);
    }


    @Transactional
    public CartPaymentResponse addToCart(Integer userId, UpdateQuantityDto newItem) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIDAndBookID(userId, newItem.getBookId())
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setBookID(Book.builder().id(newItem.getBookId()).build());
                    newCart.setUserID(User.builder().id(userId).build());
                    newCart.setQuantity(0);
                    return newCart;
                });

        shoppingCart.setQuantity(shoppingCart.getQuantity() + newItem.getNewQuantity());

        shoppingCartRepository.save(shoppingCart);

        return calculateRowTotal(userId);
    }

    private CartPaymentResponse calculateRowTotal(Integer userId) {
        List<CartResponse> carts = shoppingCartRepository.findAllCartsByUserId(userId);

        carts.forEach(cart ->
                cart.setRowTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
        );

        BigDecimal totalPayment = carts.stream()
                .map(CartResponse::getRowTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartPaymentResponse.builder()
                .cartResponses(carts)
                .totalPayment(totalPayment)
                .build();
    }


    @Transactional
    public CartPaymentResponse updateQuantityBook(Integer userId, UpdateQuantityDto updateQuantityDto) {
        List<CartResponse> carts = shoppingCartRepository.findAllCartsByUserId(userId);

        carts.stream()
                .filter(cart -> cart.getBookId().equals(updateQuantityDto.getBookId()))
                .findFirst()
                .ifPresent(cart -> cart.setQuantity(updateQuantityDto.getNewQuantity()));

        carts.forEach(cart ->
                cart.setRowTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
        );

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIDAndBookID(userId, updateQuantityDto.getBookId())
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));

        shoppingCart.setQuantity(updateQuantityDto.getNewQuantity());

        shoppingCartRepository.save(shoppingCart);

        return CartPaymentResponse.builder()
                .cartResponses(carts)
                .totalPayment(carts.stream()
                        .map(CartResponse::getRowTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Transactional
    public CartPaymentResponse deleteBookFromCart(Integer userId, Integer bookId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIDAndBookID(userId, bookId)
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));

        shoppingCartRepository.delete(shoppingCart);
        List<CartResponse> result = shoppingCartRepository.findAllCartsByUserId(userId);

        result.forEach(cart ->
                cart.setRowTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
        );
        return CartPaymentResponse.builder()
                .cartResponses(result)
                .totalPayment(result.stream()
                        .map(CartResponse::getRowTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Transactional
    public CartPaymentResponse checkOut(Integer userId) {
        List<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserID_Id(userId);

        if (!shoppingCart.isEmpty()) {
            BigDecimal totalPayment = shoppingCart.stream()
                    .map(cart -> cart.getBookID().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Order order = Order.builder()
                    .userID(User.builder().id(userId).build())
                    .totalAmount(totalPayment)
                    .status("Pending")
                    .build();

            order = orderRepository.save(order);

            Order finalOrder = order;

            List<OrderDetail> orderDetails = shoppingCart.stream()
                    .map(cart -> OrderDetail.builder()
                            .bookID(cart.getBookID())
                            .quantity(cart.getQuantity())
                            .price(cart.getBookID().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                            .orderID(finalOrder)
                            .build())
                    .toList();

            CartPaymentResponse rs = calculateRowTotal(userId);

            orderDetailRepository.saveAll(orderDetails);

            shoppingCart.forEach(cart -> bookService.descQuantity(cart.getBookID().getId(), cart.getQuantity()));

            shoppingCartRepository.deleteAll(shoppingCart);

            return rs;
        }

        return calculateRowTotal(userId);
    }

}
