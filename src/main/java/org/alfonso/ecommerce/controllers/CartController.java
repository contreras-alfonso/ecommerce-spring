package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        productVariantService.checkStockAndUpdateCart(verifyStockRequest);
        return ResponseEntity.ok(
                Map.of("msg", "Stock disponible")
        );

    }

}
