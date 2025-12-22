package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.exceptions.NoStockAvailableException;
import org.alfonso.ecommerce.services.ProductVariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyStock(
            @RequestBody VerifyStockRequest verifyStockRequest
    ) {
        productVariantService.verifyStock(verifyStockRequest);
        return ResponseEntity.ok(
                Map.of("msg", "Stock disponible")
        );

    }

}
