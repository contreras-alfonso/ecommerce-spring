package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.CartResponseDto;
import org.alfonso.ecommerce.dto.RemoveItemCartRequest;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.entities.Cart;

public interface CartService {
    CartResponseDto checkStockAndUpdateCart(VerifyStockRequest stockRequest);
    CartResponseDto removeItemFromCart(RemoveItemCartRequest removeItemCartRequest);

}
