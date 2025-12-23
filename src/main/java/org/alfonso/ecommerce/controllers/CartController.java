package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.CartResponseDto;
import org.alfonso.ecommerce.dto.FindActiveCartRequest;
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
            @Valid @RequestBody VerifyStockRequest verifyStockRequest
    ) {
        CartResponseDto response = productVariantService.checkStockAndUpdateCart(verifyStockRequest);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeItem(
            @Valid @RequestBody RemoveItemCartRequest removeItemCartRequest
    ) {
        CartResponseDto response = productVariantService.removeItemFromCart(removeItemCartRequest);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/active")
    public ResponseEntity<?> findActiveCartFromUser() {
        CartResponseDto response = productVariantService.findActiveCartFromUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/active-guest")
    public ResponseEntity<?> findActiveCart(
            @Valid @RequestBody FindActiveCartRequest request
    ) {
        CartResponseDto response = productVariantService.findActiveCart(request);
        return ResponseEntity.ok(response);
    }

}
