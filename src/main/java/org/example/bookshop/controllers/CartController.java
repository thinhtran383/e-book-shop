package org.example.bookshop.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.cart.UpdateQuantityDto;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.cart.CartPaymentResponse;
import org.example.bookshop.responses.cart.CartResponse;
import org.example.bookshop.services.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/carts")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<Response<CartPaymentResponse>> getCartsByCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        CartPaymentResponse responses = shoppingCartService.getCartsByUserId(user.getId());

        return ResponseEntity.ok(Response.<CartPaymentResponse>builder()
                .data(responses)
                .message("Success")
                .build());
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<Response<CartPaymentResponse>> updateQuantityBook(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateQuantityDto updateQuantityDto
    ) {
        CartPaymentResponse responses = shoppingCartService.updateQuantityBook(user.getId(), updateQuantityDto);

        return ResponseEntity.ok(Response.<CartPaymentResponse>builder()
                .data(responses)
                .message("Success")
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<CartPaymentResponse>> addToCart(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateQuantityDto updateQuantityDto
    ) {
        CartPaymentResponse responses = shoppingCartService.addToCart(user.getId(), updateQuantityDto);

        return ResponseEntity.ok(Response.<CartPaymentResponse>builder()
                .data(responses)
                .message("Success")
                .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response<CartPaymentResponse>> deleteBookFromCart(
            @AuthenticationPrincipal User user,
            @RequestParam Integer bookId
    ) {
        CartPaymentResponse responses = shoppingCartService.deleteBookFromCart(user.getId(), bookId);

        return ResponseEntity.ok(Response.<CartPaymentResponse>builder()
                .data(responses)
                .message("Success")
                .build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<Response<?>> checkOut(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(Response.builder()
                .message("Success")
                .data(shoppingCartService.checkOut(user.getId()))
                .build());
    }

}
