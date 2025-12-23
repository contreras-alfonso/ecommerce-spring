package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.alfonso.ecommerce.entities.StockUpdateMode;

@Data
@AllArgsConstructor
public class VerifyStockRequest {
    private StockUpdateMode mode;
    private String cartId;
    private String variantId;
    private int quantity;
}
