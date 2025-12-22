package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.entities.Cart;

public interface CartService {
    Cart checkStockAndUpdateCart(VerifyStockRequest stockRequest);

}
