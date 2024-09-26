package org.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.cart.CartPaymentResponse;
import org.example.bookshop.services.PaymentService;
import org.example.bookshop.services.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<Response<?>> payment(
            @AuthenticationPrincipal User user
    ) {

        CartPaymentResponse cartPaymentResponse = shoppingCartService.getCartsByUserId(user.getId());

        BigDecimal totalPayment = cartPaymentResponse.getTotalPayment();

        return ResponseEntity.ok(Response.builder()
                .data(paymentService.payWithMoMo(UUID.randomUUID().toString().substring(7), totalPayment, user.getId()))
                .message("Payment")
                .build()
        );
    }


    @Hidden
    @PostMapping("/ipn/{userId}")
    public ResponseEntity<Response<?>> callBackMomo(
            @PathVariable Integer userId
    ) {
        shoppingCartService.checkOut(userId);

        System.out.println("IPN");

        return ResponseEntity.ok(Response.builder()
                .message("IPN")
                .build());
    }
}