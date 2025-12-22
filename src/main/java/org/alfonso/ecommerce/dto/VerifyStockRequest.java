package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyStockRequest {
    private String variantId;
    private int quantity;
}
