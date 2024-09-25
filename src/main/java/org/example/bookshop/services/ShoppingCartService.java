package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.cart.UpdateQuantityDto;
import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.ShoppingCart;
import org.example.bookshop.entities.User;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.IShoppingCartRepository;
import org.example.bookshop.responses.cart.CartPaymentResponse;
import org.example.bookshop.responses.cart.CartResponse;
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

    @Transactional(readOnly = true)
    public CartPaymentResponse getCartsByUserId(Integer userId) {
        List<CartResponse> carts = shoppingCartRepository.findAllCartsByUserId(userId);

        carts.forEach(cart -> {
            cart.setRowTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        });

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
}
