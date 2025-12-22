package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.VerifyStockRequest;

public interface ProductVariantService {
    void verifyStock(VerifyStockRequest stockRequest);
}
