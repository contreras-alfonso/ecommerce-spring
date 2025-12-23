package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.CartResponseDto;
import org.alfonso.ecommerce.dto.RemoveItemCartRequest;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService productVariantService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyStock(
            @RequestBody VerifyStockRequest verifyStockRequest
    ) {
        CartResponseDto response = productVariantService.checkStockAndUpdateCart(verifyStockRequest);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeItem(
            @RequestBody RemoveItemCartRequest removeItemCartRequest
    ) {
        CartResponseDto response = productVariantService.removeItemFromCart(removeItemCartRequest);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/active")
    public ResponseEntity<?> findActiveCart() {
        CartResponseDto response = productVariantService.findActiveCart();
        return ResponseEntity.ok(response);
    }

}
